package jsonrpc.authserver.service;

import jsonrpc.authserver.entities.Role;
import jsonrpc.authserver.entities.User;
import jsonrpc.authserver.entities.token.Token;
import jsonrpc.authserver.entities.token.AccessToken;
import jsonrpc.authserver.entities.token.RefreshToken;
import jsonrpc.authserver.repository.token.AccessTokenRepository;
import jsonrpc.authserver.repository.token.RefreshTokenRepository;
import jsonrpc.protocol.http.OauthResponse;
import jsonrpc.protocol.token.TokenType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.invoke.MethodHandles;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static jsonrpc.authserver.config.SpringConfiguration.ISSUER;

@Service
@Transactional
public class TokenService {

    private final static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final JwtTokenService jwtTokenService;
    private final UserService userService;
    private final AccessTokenRepository accessTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BlacklistTokenService blacklistTokenService;

    @Autowired
    public TokenService(JwtTokenService jwtTokenService,
                        UserService userService,
                        AccessTokenRepository accessTokenRepository,
                        RefreshTokenRepository refreshTokenRepository,
                        BlacklistTokenService blacklistTokenService) {

        this.jwtTokenService = jwtTokenService;
        this.userService = userService;
        this.accessTokenRepository = accessTokenRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.blacklistTokenService = blacklistTokenService;
    }


    /**
     * Create new token, delete previous(on refreshing)
     *
     * @param userName
     * @param refreshToken current refresh_token if available
     * @return
     */
    public OauthResponse issueTokens(String userName, RefreshToken refreshToken) {


        boolean isTokenApproved = refreshToken != null && refreshToken.isEnabled();

        RefreshToken oldRefreshToken = refreshToken;
        AccessToken accessToken = null;
        //
        String accessTokenString = null;
        String refreshTokenString = null;
        //
        //


        // find user
        User user = userService.findByName(userName).orElseThrow(
                () -> new UsernameNotFoundException("User not exists: " + userName));


        // 1. Refresh Token -------------------------------------------------------------------

        long ttl = isTokenApproved ? TokenType.REFRESH.getTtl() : 1800;
        Instant expiredAt = Instant.now().plusSeconds(ttl);

        refreshToken = new RefreshToken(user, isTokenApproved, expiredAt);
        refreshTokenRepository.save(refreshToken);

        Set<String> refreshRoles = new HashSet<>(Collections.singletonList(Role.REFRESH));

        refreshTokenString = jwtTokenService.createJWT(
                TokenType.REFRESH, refreshToken.getId(), ISSUER, user.getName(), refreshRoles, ttl);

        // 2. Access Token ---------------------------------------------------------------------


        if (isTokenApproved) {

            expiredAt = Instant.now().plusSeconds(TokenType.ACCESS.getTtl());
            accessToken = new AccessToken(user, true, refreshToken, expiredAt);
            refreshToken.setAccessToken(accessToken);
            
            accessTokenRepository.save(accessToken);

            Set<String> roles =
                    user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());

            accessTokenString = jwtTokenService.createJWT(
                    TokenType.ACCESS, refreshToken.getId(), ISSUER, user.getName(), roles, TokenType.ACCESS.getTtl());

        }

        // Delete here deprecated access and refresh token --------------------------------------

        if (oldRefreshToken != null) {
            AccessToken oldAccessToken = oldRefreshToken.getAccessToken();
            if (oldAccessToken != null) {
                blacklistTokenService.blacklist(oldAccessToken);
            }
            // will delete both token(old refresh and old access if has)
            delete(oldRefreshToken);
        }
        return new OauthResponse(accessTokenString, refreshTokenString);
    }


    public AccessToken findAccessToken(Long id) {

        AccessToken result = null;

        if (id != null) {
            result = accessTokenRepository.findById(id).orElse(null);
        }
        return result;
    }


    public RefreshToken findRefreshToken(Long id) {

        RefreshToken result = null;

        if (id != null) {
            result = refreshTokenRepository.findById(id).orElse(null);
        }
        return result;
    }


    public void approveToken(RefreshToken token) {

        assert token != null;
        assert token.getId() != null;
        refreshTokenRepository.approveById(token.getId());
    }


    public void delete(Token token) {

        try {

            if (token instanceof AccessToken) {
                accessTokenRepository.delete((AccessToken) token);
            } else if (token instanceof RefreshToken) {
                refreshTokenRepository.delete((RefreshToken) token);
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }


    public void vacuum() {

        accessTokenRepository.vacuum();
        refreshTokenRepository.vacuum();

    }
}



//        Long accessS = Instant.now().getEpochSecond() - TokenType.ACCESS.getTtl();
//        Instant access = Instant.ofEpochSecond(accessS);
//
//        Long refreshS = Instant.now().getEpochSecond() - TokenType.REFRESH.getTtl();
//        Instant refresh = Instant.ofEpochSecond(refreshS);
//
//        tokenRepository.vacuum(access, refresh);






//    /**
//     * Return token that client used in bearer authentication (if has)
//     */
//    public Token getTokenUsedInAuthentication() {
//        return findById(getAuthTokenId());
//    }



//    /**
//     * Verify token type (if exists)
//     * <br> Token retrieved from session (originated from authentication)
//     * @param tokenType TokenType
//     * @param role role, issued to token
//     * @Throw AccessDeniedException if conditions failed. do nothing if token not exists.
//     */
//    public Long checkTokenAuthorization(TokenType tokenType, Set<String> roles) {
//
//        Long result = null;
//
//        ServletRequestAttributes attr = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
//        HttpSession session = attr.getRequest().getSession();
//
//        Claims claims = (Claims)session.getAttribute("claims");
//
//        // user has authorised by TOKEN - check it
//        if (claims != null) {
//
//            result = Long.valueOf(claims.getId());
//            Set<String> authorities = rolesToSet(claims.get("authorities"));
//
//            // token type and role
//            if (!claims.get("type").equals(tokenType.getName()) ||
//                !authorities.containsAll(roles)) {
//
//                throw new AccessDeniedException("Token unauthorized");
//            }
//
//        }
//
//        return result;
//    }







////user = new User(userDetails.getUsername(), "[PROTECTED]", authorities);
//        cUser = userService.findByName(userDetails.getUsername()).orElseThrow(
//                () -> new RuntimeException("User not exists: " + userDetails.getUsername()));
//
//
//
//
//
////cuser.setRolesEx(userDetails.getAuthorities());
//
////authorities = new ArrayList<>(userDetails.getAuthorities());
////user = new User(userDetails.getUsername(), "[PROTECTED]", authorities);

//authorities.add(new SimpleGrantedAuthority(ROLE_PREFIX + "REFRESH"));


//    public boolean isTokenEnabled(Long id) {
//
//        AtomicBoolean result = new AtomicBoolean(false);
//        if (id != null) {
//            tokenRepository.findById(id).ifPresent(token -> result.set(token.isEnabled()));
//        }
//        return result.get();
//    }




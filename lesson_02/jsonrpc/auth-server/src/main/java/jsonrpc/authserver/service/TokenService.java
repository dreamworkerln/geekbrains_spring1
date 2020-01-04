package jsonrpc.authserver.service;

import io.jsonwebtoken.Claims;
import jsonrpc.authserver.entities.Role;
import jsonrpc.authserver.entities.User;
import jsonrpc.authserver.entities.Token;
import jsonrpc.authserver.repository.TokenRepository;
import jsonrpc.protocol.http.OauthResponse;
import jsonrpc.protocol.http.TokenType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static jsonrpc.authserver.config.SpringConfiguration.ISSUER;
import static jsonrpc.utils.Utils.rolesToSet;

@Service
@Transactional
public class TokenService {

    private final static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final JwtTokenService jwtTokenService;
    private final UserService userService;
    private final TokenRepository tokenRepository;

    @Autowired
    public TokenService(JwtTokenService jwtTokenService, TokenRepository tokenRepository, UserService userService) {
        this.jwtTokenService = jwtTokenService;
        this.tokenRepository = tokenRepository;
        this.userService = userService;
    }


    /**
     * Create new tokens, delete previous(on refreshing)
     * @param userDetails UserDetails
     * @param refreshToken current refresh_token of available
     * @return
     */
    public OauthResponse issueTokens(String userName, Token refreshToken) {

        if (refreshToken!=null && refreshToken.getType() != TokenType.REFRESH) {
            throw new IllegalArgumentException("token.type should be refresh_token");
        }

        boolean isTokenApproved = refreshToken != null && refreshToken.isEnabled();

        Token oldRefreshToken = refreshToken;
        Token accessToken = null;
        //
        String accessString = null;
        String refreshString;
        //
        //


        // find user
        User user = userService.findByName(userName).orElseThrow(
                () -> new UsernameNotFoundException("User not exists: " + userName));

        Set<String> roles =
                user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());


        // 1. Access Token ---------------------------------------------------------------------


        if (isTokenApproved) {
            accessToken = new Token(TokenType.ACCESS, user, true);
            tokenRepository.save(accessToken);

            accessString = jwtTokenService.createJWT(
                TokenType.ACCESS, refreshToken.getId(), ISSUER, user.getName(), roles, TokenType.ACCESS.getTtl());

        }

        // 2. Refresh Token -------------------------------------------------------------------

        long ttl = isTokenApproved ? TokenType.REFRESH.getTtl(): 1800;

        refreshToken = new Token(TokenType.REFRESH, user, accessToken, isTokenApproved);
        tokenRepository.save(refreshToken);

        refreshString = jwtTokenService.createJWT(
            TokenType.REFRESH, refreshToken.getId(), ISSUER, user.getName(), roles, ttl);

        // Delete here deprecated access and refresh tokens -------------------------
        if (oldRefreshToken != null) {
            delete(oldRefreshToken);

            if (oldRefreshToken.getAccessToken() != null) {
                delete(oldRefreshToken.getAccessToken());
            }
        }
        return new OauthResponse(accessString, refreshString);
    }


    public boolean isTokenEnabled(Long id) {

        AtomicBoolean result = new AtomicBoolean(false);
        if (id != null) {
            tokenRepository.findById(id).ifPresent(token -> result.set(token.isEnabled()));
        }
        return result.get();
    }


    public Token findById(Long id) {

        Token result = null;

        if (id!= null) {
            result = tokenRepository.findById(id).orElse(null);
        }
        return result;
    }


    public void approveToken(Token token) {

        if (token.getType() != TokenType.REFRESH) {
            throw new IllegalArgumentException("Bat token type, should be refresh_token");
        }

        if (token.getId() != null) {
            tokenRepository.ApproveTokenById(token.getId());
        }
    }


    public void delete(Token token) {
        try {
            tokenRepository.deleteById(token.getId());
        }
        catch (Exception e) {
            log.error("", e);
        }
    }


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


    public Long getAuthTokenId() {

        Long result = null;

        ServletRequestAttributes attr = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession();
        Claims claims = (Claims)session.getAttribute("claims");

        if (claims != null) {
            result = Long.valueOf(claims.getId());
        }

        return result;
    }

    /**
     * Return token that client used in authentication (if has)
     */
    public Token getAuthToken() {
        return findById(getAuthTokenId());
    }

}







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




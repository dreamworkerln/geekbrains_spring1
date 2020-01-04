package jsonrpc.authjwtserver.service;

import jsonrpc.authjwtserver.config.SpringConfiguration;
import jsonrpc.authjwtserver.entities.Role;
import jsonrpc.authjwtserver.entities.User;
import jsonrpc.authjwtserver.entities.Token;
import jsonrpc.authjwtserver.repository.TokenRepository;
import jsonrpc.protocol.http.OauthResponse;
import jsonrpc.protocol.http.TokenType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static jsonrpc.authjwtserver.config.SpringConfiguration.ISSUER;

@Service
@Transactional
public class TokenService {

    private final JwtTokenService jwtTokenService;
    private final UserService userService;
    private final TokenRepository tokenRepository;

    @Autowired
    public TokenService(JwtTokenService jwtTokenService, TokenRepository tokenRepository, UserService userService) {
        this.jwtTokenService = jwtTokenService;
        this.tokenRepository = tokenRepository;
        this.userService = userService;
    }


    public OauthResponse issueTokens(UserDetails userDetails, Long tokenId) {

        // tokenId - Access token id

        User user; // do not save it to DB !!! (used only for token generation)
        Token token = null;
        String accessToken = null;
        String refreshToken = null;
        long ttl;
        TokenType tokenType;
        //
        //
        boolean isTokenApproved = (tokenId!= null) && isTokenApproved(tokenId);

        // find user
        user = userService.findByName(userDetails.getUsername()).orElseThrow(
                () -> new UsernameNotFoundException("User not exists: " + userDetails.getUsername()));


        // 1. Access Token ---------------------------------------------------------------------
        tokenType = TokenType.ACCESS;
        ttl = tokenType.getTtl();

        if (isTokenApproved) {

            token = new Token(user, true);
            tokenRepository.save(token);

            Set<String> roles =
                    user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());

            accessToken = jwtTokenService.createJWT(
                    tokenType, token.getId(), ISSUER, user.getName(), roles,ttl);

        }

        // 2. Refresh Token -------------------------------------------------------------------
        tokenType = TokenType.REFRESH;
        ttl = tokenType.getTtl();

        if (!isTokenApproved) {
            ttl = 1800;
        }

        token = new Token(user, isTokenApproved, token);
        tokenRepository.save(token);


        Set<String> roles = new HashSet<>();
        roles.add(Role.REFRESH);

        refreshToken = jwtTokenService.createJWT(
                tokenType, token.getId(), ISSUER, user.getName(), roles, ttl);


        // Delete here replaced(deprecated) access and refresh tokens -------------------------
        if (tokenId != null) {
            tokenRepository.findById(tokenId).ifPresent(this::delete);
        }

        return new OauthResponse(accessToken, refreshToken);
    }

    
    public boolean isTokenApproved(Long id) {

        AtomicBoolean result = new AtomicBoolean(false);
        tokenRepository.findById(id).ifPresent(token -> result.set(token.getApproved()));
        return result.get();
    }


    public Optional<Token> findById(Long id) {
        return tokenRepository.findById(id);
    }


    public void approveToken(Long id) {
        tokenRepository.ApproveTokenById(id);
    }


    public void delete(Token token) {

        Token accessToken = token.getAccessToken();

        if (accessToken != null) {
            tokenRepository.deleteById(accessToken.getId());
        }
        tokenRepository.deleteById(token.getId());
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




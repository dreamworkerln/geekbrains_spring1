package jsonrpc.authserver.controller;

import jsonrpc.authserver.config.RequestScopeBean;
import jsonrpc.authserver.entities.Role;
import jsonrpc.authserver.entities.token.Token;
import jsonrpc.authserver.entities.token.AccessToken;
import jsonrpc.authserver.entities.token.RefreshToken;
import jsonrpc.authserver.service.BlacklistTokenService;
import jsonrpc.authserver.service.TokenService;
import jsonrpc.protocol.http.BlackListResponse;
import jsonrpc.protocol.http.OauthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;


// handmadezz oauzz


@RestController
@RequestMapping("/oauzz/token/")
@CrossOrigin
public class OauthController {

    private final static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final TokenService tokenService;
    private final BlacklistTokenService blacklistTokenService;
    private final RequestScopeBean requestScopeBean;

    public OauthController(TokenService tokenService, BlacklistTokenService blacklistTokenService, RequestScopeBean requestScopeBean) {
        this.tokenService = tokenService;
        this.blacklistTokenService = blacklistTokenService;
        this.requestScopeBean = requestScopeBean;
    }




    /**
     * Obtain new access and refresh token by BasicAuth
     */
    @PostMapping(value = "/get")
    public ResponseEntity<OauthResponse> getToken() {

        OauthResponse result;

        // Тот, кто зашел по паролю получает урезанный по времени жизни REFRESH токен.
        // Если пользователь подтвердит этого клиента
        // (через другое, доверенное приложение-клиент - мобильное приложение),
        // то клиент сможет обновить токен и получить пару нормальных токенов ACCESS + REFRESH
        //
        // Клиенту перед upgrade(refresh) урезанного refresh токена стоит проверить, что он был подтвержден.
        // Иначе клиент получит новый урезанный refresh_token, который надо будет заново подтверждать.

        result = tokenService.issueTokens(getUsername(), null);

        return ResponseEntity.ok(result);
    }


    /**
     * Refresh tokens by Bearer refresh_token
     */
    @PostMapping(value = "/refresh")
    @Secured({Role.REFRESH})
    public ResponseEntity<OauthResponse> refreshToken() {

        OauthResponse result;

        RefreshToken refreshToken = getTokenFromAuthentication(RefreshToken.class);

        // пара нормальных токенов ACCESS + REFRESH выдается только для подтвержденного refresh_token
        // для неподтвержденного refresh_token будет выдан такой же урезанный refresh_token
        //
        // refresh_token is approved == (refresh_token.isEnabled == true)
        result = tokenService.issueTokens(getUsername(), refreshToken);

        return ResponseEntity.ok(result);
    }


    /**
     * Check if refresh_token is approved by confidential client
     * <br>Bearer refresh_token Authorization only
     */
    @PostMapping(value = "/check_is_approved")
    @Secured({Role.REFRESH})
    public ResponseEntity checkIsTokenApproved() {

        HttpStatus status;

        RefreshToken refreshToken = getTokenFromAuthentication(RefreshToken.class);

        status = refreshToken.isEnabled() ? HttpStatus.OK : HttpStatus.NO_CONTENT;

        return new ResponseEntity(status);
    }


    /**
     * Approve refresh_token
     * <br> Allow Basic and Bearer access_token Authorization
     */
    @PostMapping("/approve")
    @Secured({Role.USER, Role.ADMIN})
    public ResponseEntity approveToken(@Param("id") Long id) {

        // check if user authenticated by access_token
        // then make sure that token type is access_token_type
        if (isBearerAuthentication()) {
            getTokenFromAuthentication(AccessToken.class);
        }

        // ------------------------------------------------------------

        // переходим к управлению refresh_token, который надо включить
        RefreshToken refreshToken = tokenService.findRefreshToken(id);

        if (refreshToken == null) {
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        }

        // Пользователь хочет манипулировать не своим токеном
        if (!refreshToken.getUser().getName().equals(getUsername())) {
            throw new AccessDeniedException("It's not you token");
        }

        // Set token.enabled = true
        tokenService.approveToken(refreshToken);

        return new ResponseEntity(null, HttpStatus.OK);
    }



    /**
     * Return blacklisted access_tokens
     * @param from from that index to last available (from is not token id)
     * @return List of denied token id
     */
    @PostMapping(value = "/listblack")
    @Secured({Role.RESOURCE, Role.ADMIN})
    public ResponseEntity<BlackListResponse> getBlackList(@Param("from") Long from) {

        BlackListResponse result = new BlackListResponse();

        // check if user authenticated by access_token
        // then make sure that token type is access_token_type
        if (isBearerAuthentication()) {
            getTokenFromAuthentication(AccessToken.class);
        }

        result.setList(blacklistTokenService.getFrom(from));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    // ==============================================================================

    private boolean isBearerAuthentication() {
        return requestScopeBean.getAuthenticationType() == RequestScopeBean.AuthenticationType.BEARER;
    }


    private <T extends Token> T getTokenFromAuthentication(Class<T> tokenClass) {

        Token token = requestScopeBean.getToken();
        // check wrong token type (access instead refresh and vise versa)
        if (token.getClass() != tokenClass) {
            throw new AccessDeniedException("Unauthenticated");
        }
        return tokenClass.cast(token);
    }


    // get current user name
    private static String getUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails)principal;
        return userDetails.getUsername();
    }

}


    // ---------------------------------------------------------------------------------------


//    private static UsernamePasswordAuthenticationToken getAuthentication() {
//
//        UsernamePasswordAuthenticationToken result = null;
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication instanceof UsernamePasswordAuthenticationToken) {
//            result = (UsernamePasswordAuthenticationToken)authentication;
//        }
//        return result;
//    }



//    // get current session
//    private static HttpSession getSession() {
//        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
//        return attr.getRequest().getSession(); // true == allow create
//    }


//    /**
//     * Return token id that user used in authentication
//     * <br>If has one
//     */
//    private Long getUsedInAuthenticationTokenId() {
//
//        Long result = null;
//
//        ServletRequestAttributes attr = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
//        HttpSession session = attr.getRequest().getSession();
//        Claims claims = (Claims)session.getAttribute("claims");
//
//        if (claims != null) {
//            result = Long.valueOf(claims.getId());
//        }
//        return result;
//    }


//    /**
//     * Return token that user used in authentication
//     * <br>If has one and token is permitted by server
//     */
//    private Token getUsedInAuthenticationToken() {
//        return tokenService.findById(getUsedInAuthenticationTokenId());
//    }



//    /**
//     * Check token type(if token present)
//     */
//    private void  checkTokenType(Token token, TokenType type) {
//        if (token != null && token.getType() != type) {
//            log.info("Wrong token type was: {}, should be: {}", token.getType(), type);
//            throw new AccessDeniedException("Unauthenticated");
//        }
//    }
//
//
//    /**
//     * Check tokem presence
//     */
//    private void  checkTokenPresent(Token token) {
//        if (token == null) {
//            log.info("Token should be present, but it was null");
//            throw new AccessDeniedException("Unauthenticated");
//        }
//    }









// ---------------------------------------------------------------------------------------


// @RequestParam("grant_type") String grantTypeStr
//GrantType grantType = GrantType.parse(grantTypeStr); // ??


//    /**
//     * Check that token, used in authentication has correct type
//     * @param type TokenType
//     * @return Token
//     */
//    private Token checkTokenType(TokenType type) {
//
//        Token result = tokenService.findById(getUsedInAuthenticationTokenId());
//        // checking token type
//        if (result != null && result.getType() != type) {
//            // Wrong token type
//            throw new AccessDeniedException("Unauthenticated");
//        }
//        return result;
//    }



// SecurityContextHolder.getContext().getAuthentication().getAuthorities();
//final UserDetails userDetails = userDetailsService.loadUserByUsername(userDetailsContext.getUsername());

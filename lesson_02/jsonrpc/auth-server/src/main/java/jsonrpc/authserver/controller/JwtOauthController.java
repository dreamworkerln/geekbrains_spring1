package jsonrpc.authserver.controller;

import io.jsonwebtoken.Claims;
import jsonrpc.authserver.entities.Token;
import jsonrpc.authserver.service.BlacklistTokenService;
import jsonrpc.authserver.service.TokenService;
import jsonrpc.protocol.http.BlackListResponse;
import jsonrpc.protocol.http.OauthResponse;
import jsonrpc.protocol.http.TokenType;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;


// handmadezz oauzz


@RestController
@RequestMapping("/oauzz/token/")
@CrossOrigin
public class JwtOauthController {

    private final static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final TokenService tokenService;

    private final BlacklistTokenService blacklistTokenService;

    public JwtOauthController(TokenService tokenService, BlacklistTokenService blacklistTokenService) {
        this.tokenService = tokenService;
        this.blacklistTokenService = blacklistTokenService;
    }




    /**
     * Obtain new access and refresh tokens by BasicAuth or
     * Refresh and get new acces and refresh tokens by Bearer refresh_token
     */
    @PostMapping(value = "/get")
    public ResponseEntity<OauthResponse> getToken() {

        OauthResponse result;


        // @RequestParam("grant_type") String grantTypeStr
        //GrantType grantType = GrantType.parse(grantTypeStr); // ??

        // Пользователь мой зайти сюда используя Authentication:
        // BasicAuth
        // Bearer (refresh_token)

        // Если пользователь использовал refresh_token для аутентификации
        Token refreshToken = checkTokenType(TokenType.REFRESH);

        // Тот, кто зашел по паролю получает урезаннй по времени жизни REFRESH токен.
        // Если пользователь подтвердит этого клиента
        // (через другое, доверенное приложение-клиент - мобильное приложение),
        // то клиент сможет обновить токен и получить пару нормальных токенов ACCESS + REFRESH
        //
        // Клиенту перед upgrade(refresh) урезанного refresh токена стоит проверить,
        // что он был подтвержден.
        // Иначе клиент получит новый урезанный refresh_token,
        // который снова надо будет заново подтверждать.

        result = tokenService.issueTokens(getUsername(), refreshToken);

        return ResponseEntity.ok(result);
    }


    /**
     * Check if refresh_token is approved by confidential client
     * <br>Bearer refresh_token Authorization only
     */
    @PostMapping(value = "/check_is_approved")
    public ResponseEntity checkTokenApproved() {

        HttpStatus status;

        // Пользователь  обязан использовать refresh_token для аутентификации
        Token refreshToken = checkTokenType(TokenType.REFRESH);

        status = refreshToken.isEnabled() ? HttpStatus.OK : HttpStatus.NO_CONTENT;

        return new ResponseEntity(status);
    }


    /**
     * Approve refresh_token
     * <br> Allow Basic and Bearer access_token Authorisation
     */
    @PostMapping("/approve")
    @Secured({"ROLE_USER","ROLE_ADMIN"})
    public ResponseEntity approveToken(@Param("id") Long id) {

        // Пользователь мой зайти сюда используя Authentication:
        // BasicAuth
        // Bearer (access_token)

        // check if user authenticated by access_token
        checkTokenType(TokenType.REFRESH);

        // переходим к управлению refresh_token, который надо включить
        Token refreshToken = tokenService.findById(id);

        if (refreshToken == null) {
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        }

        // Пользователь хочет манипулировать не своим токеном
        if (!refreshToken.getUser().getName().equals(getUsername())) {
            throw new AccessDeniedException("It's not you token");
        }

        tokenService.approveToken(refreshToken);

        return new ResponseEntity(null, HttpStatus.OK);
    }


    /**
     * Return blacklisted access_tokens
     * @param fromId from that index to last available (not token id)
     * @return List of token id
     */
    @PostMapping(value = "/blackList")
    @Secured({"ROLE_RESOURCE","ROLE_ADMIN"})
    public ResponseEntity<BlackListResponse> getBlackList(@Param("from") Long from) {

        HttpStatus status;

        BlackListResponse result = new BlackListResponse();

        // check if user authenticated by access_token
        checkTokenType(TokenType.REFRESH);

        result.setList(blacklistTokenService.getFrom(from));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    // ==============================================================================


    // get current session
    private static HttpSession getSession() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(); // true == allow create
    }


    // get current user name
    private static String getUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails)principal;
        return userDetails.getUsername();
    }


    // ---------------------------------------------------------------------------------------


    private Long getUsedInAuthenticationTokenId() {

        Long result = null;

        ServletRequestAttributes attr = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession();
        Claims claims = (Claims)session.getAttribute("claims");

        if (claims != null) {
            result = Long.valueOf(claims.getId());
        }

        return result;
    }


    // ---------------------------------------------------------------------------------------

    /**
     * Check that token, used in authentication has correct type
     * @param type TokenType
     * @return Token
     */
    private Token checkTokenType(TokenType type) {

        Token result = tokenService.findById(getUsedInAuthenticationTokenId());
        // checking token type
        if (result != null && result.getType() != type) {
            // Wrong token type
            throw new AccessDeniedException("Unauthenticated");
        }
        return result;
    }

}

// SecurityContextHolder.getContext().getAuthentication().getAuthorities();
//final UserDetails userDetails = userDetailsService.loadUserByUsername(userDetailsContext.getUsername());

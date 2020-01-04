package jsonrpc.authserver.controller;

import io.jsonwebtoken.Claims;
import jsonrpc.authserver.entities.Token;
import jsonrpc.protocol.http.TokenCheckResult;
import jsonrpc.authserver.entities.Role;
import jsonrpc.authserver.service.TokenService;
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
import java.util.*;

import static jsonrpc.utils.Utils.grantedAuthorityToSet;


@RestController
@RequestMapping("/oauzz/token/")
@CrossOrigin
public class JwtOauthController {

    private final static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final TokenService tokenService;

    public JwtOauthController(TokenService tokenService) {
        this.tokenService = tokenService;
    }


    // handmadezz oauzz

    /**
     * Obtain new tokens by BasicAuth or
     * Refresh by Bearer refresh_token
     */
    @PostMapping(value = "/get")
    public ResponseEntity<OauthResponse> getToken() {

        OauthResponse result;
        Token refreshToken;

        // @RequestParam("grant_type") String grantTypeStr
        //GrantType grantType = GrantType.parse(grantTypeStr); // ??

        refreshToken = tokenService.getAuthToken();
        // Если пользователь использовал refresh_token для аутентификации
        if (refreshToken != null && refreshToken.getType() != TokenType.REFRESH) {
          throw new AccessDeniedException("Token unauthenticated");
        }

        // Тот, кто зашел по паролю получает урезаннй по времени жизни REFRESH токен.
        // Если пользователь подтвердит этого клиента
        // (через другое, доверенное приложение-клиент - мобильное приложение),
        // то клиент сможет обновить токен и получить пару нормальных токенов ACCESS + REFRESH
        //
        // Клиенту перед обновлением токена стоит проверить, что токен был подтвержден.
        // Иначе клиент получит новый урезанный refresh_token,
        // который снова надо будет заново подтверждать.
        result = tokenService.issueTokens(getUsername(), refreshToken);

        return ResponseEntity.ok(result);
    }


    /**
     * Check if refresh_token is approved by confidential client
     * <br>Only Bearer refresh_token Authorization
     */
    @PostMapping(value = "/check_approved")
    public ResponseEntity checkTokenApproved() {

        HttpStatus status;

        Token refreshToken = tokenService.getAuthToken();
        if (refreshToken == null || refreshToken.getType() != TokenType.REFRESH) {
            throw new AccessDeniedException("Token unauthenticated/no token");
        }

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

        //ResponseEntity result;

        // check if user authenticated by access_token
        Token accessToken = tokenService.getAuthToken();

        // Если пользователь использовал access_token для аутентификации
        if (accessToken != null && accessToken.getType() != TokenType.ACCESS) {
            throw new AccessDeniedException("Token unauthenticated");
        }

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


    // ==============================================================================


    // ---------------------------------------------------------------------------------------


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



}

// SecurityContextHolder.getContext().getAuthentication().getAuthorities();
//final UserDetails userDetails = userDetailsService.loadUserByUsername(userDetailsContext.getUsername());

package jsonrpc.authjwtserver.controller;

import jsonrpc.authjwtserver.entities.Role;
import jsonrpc.authjwtserver.service.TokenService;
import jsonrpc.protocol.http.GrantType;
import jsonrpc.protocol.http.OauthResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;


@RestController
@CrossOrigin
public class JwtOauthController {

    private final TokenService tokenService;

    public JwtOauthController(TokenService tokenService) {
        this.tokenService = tokenService;
    }


    //private final UserDetailsService userDetailsService;



    // handmadezz

    @PostMapping(value = "oauzz/token")
    public ResponseEntity<OauthResponse> getToken(@RequestParam("grant_type") String grantTypeStr) {

        OauthResponse result;

        GrantType grantType = GrantType.parse(grantTypeStr); // ??

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails)principal;

        Long tokenId = null;

        // get refresh_token id
        HttpSession session = session();
        if (session != null) {
            tokenId = Long.parseLong((String)session().getAttribute("tokenId"));
        }

        // Тот, кто зашел по паролю получает обрезанный REFRESH токен, который ничего не может
        // кроме как использовать его для обновления.
        // Если пользователь подтвердит этого клиента (через другое, доверенное приложение-клиент (мобильное приложение)),
        // то клиент сможет обновить токен и получить пару нормальных токенов ACCESS + REFRESH

        result = tokenService.issueTokens(userDetails, tokenId);

        return ResponseEntity.ok(result);
    }

    
    // ---------------------------------------------------------------------------------------



    private static HttpSession session() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(false); // true == allow create
    }


    // ---------------------------------------------------------------------------------------




    private OauthResponse getNewToken() {

        OauthResponse result = null;

        return result;
    }

    private OauthResponse refreshToken() {

        OauthResponse result = null;

        return result;
    }

}

// SecurityContextHolder.getContext().getAuthentication().getAuthorities();
//final UserDetails userDetails = userDetailsService.loadUserByUsername(userDetailsContext.getUsername());

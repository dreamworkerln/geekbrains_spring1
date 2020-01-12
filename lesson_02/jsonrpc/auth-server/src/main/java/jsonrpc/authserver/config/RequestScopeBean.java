package jsonrpc.authserver.config;

import jsonrpc.authserver.entities.token.AccessToken;
import jsonrpc.authserver.entities.token.RefreshToken;
import jsonrpc.authserver.entities.token.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;

// https://www.baeldung.com/spring-bean-scopes
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RequestScopeBean {

    private final HttpServletRequest request;

    private Token token;

    private AuthType authType = AuthType.BASIC_AUTH;

    @Autowired
    public RequestScopeBean(HttpServletRequest request) {
        this.request = request;
    }

//    public Token getToken() {
//        return token;
//    }

    public void setToken(Token token) {
        this.token = token;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public AuthType getAuthType() {return authType;}

    public void setAuthType(AuthType authType) {
        this.authType = authType;
    }



    public AccessToken getAccessToken() {

        return getToken(AccessToken.class);
    }

    public RefreshToken getRefreshToken() {

        return getToken(RefreshToken.class);
    }


    // ---------------------------------------------------------

    private <T extends Token> T getToken(Class<T> tokenClass) {

        // check wrong token type (access instead refresh and vise versa)
        if (token.getClass() != tokenClass) {
            throw new AccessDeniedException("Unauthenticated");
        }
        return tokenClass.cast(token);
    }




}


//    private <T extends Token> T getToken(Class<T> tokenClass) {
//
//        Token token = requestScopeBean.getToken();
//        // check wrong token type (access instead refresh and vise versa)
//        if (token.getClass() != tokenClass) {
//            throw new AccessDeniedException("Unauthenticated");
//        }
//        return tokenClass.cast(token);
//    }

//    @PostConstruct
//    void postConstruct() {}



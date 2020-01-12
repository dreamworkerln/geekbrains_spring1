package jsonrpc.authserver.config;

import jsonrpc.authserver.entities.token.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

// https://www.baeldung.com/spring-bean-scopes
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RequestScopeBean {

    private final HttpServletRequest request;

    private Token token;

    private AuthenticationType authenticationType = AuthenticationType.BASIC_AUTH;

    @Autowired
    public RequestScopeBean(HttpServletRequest request) {
        this.request = request;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public AuthenticationType getAuthenticationType() {return authenticationType;}

    public void setAuthenticationType(AuthenticationType authenticationType) {
        this.authenticationType = authenticationType;
    }

    public enum AuthenticationType {
        BASIC_AUTH,
        BEARER
    }

}

//    @PostConstruct
//    void postConstruct() {}



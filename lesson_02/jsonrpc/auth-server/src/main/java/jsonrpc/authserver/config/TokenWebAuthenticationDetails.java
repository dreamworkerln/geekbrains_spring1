package jsonrpc.authserver.config;

import jsonrpc.authserver.entities.token.Token;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

public class TokenWebAuthenticationDetails extends WebAuthenticationDetails {

    private Token token;

    /**
     * Records the remote address and will also set the session Id if a session already
     * exists (it won't create one).
     *
     * @param request that the authentication request was received from
     */
    public TokenWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }
}

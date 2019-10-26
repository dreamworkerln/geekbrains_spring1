package jsonrpc.protocol.dto.base.jrpc;

import jsonrpc.protocol.dto.base.Message;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
/**
 * Base json-rps request, contains access token and method name.
 * <br>(This api is supposed to be called from web-server, not directly by clients)
 * <br> But this may be adopted to be called by clients
 */
public class JrpcRequestHeader extends Message {

    protected String token;
    protected String method;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

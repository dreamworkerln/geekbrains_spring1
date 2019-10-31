package jsonrpc.protocol.dto.base.jrpc;

import jsonrpc.protocol.dto.base.Message;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


/**
 * Base json-rps request header, contains access token and method name( param not included)
 * <br> Not contains request param
 * <br>(This api is supposed to be called from web-server, not directly by clients)
 * <br> But this may be adopted to be called by clients
 * <br> Используется в ApiController, чтобы прочитать только заголовок jrpc запроса.
 * <br> (Не читая param запроса, т.к. десериализацией занимается конкретный handler)
 */
@Component
@Scope("prototype")
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

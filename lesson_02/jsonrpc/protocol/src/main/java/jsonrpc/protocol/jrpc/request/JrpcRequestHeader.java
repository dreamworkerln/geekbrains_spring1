package jsonrpc.protocol.jrpc.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jsonrpc.protocol.jrpc.JrpcBase;


/**
 * Base json-rps request that contains only version, id,  method name
 * <br>(param not included)
 * <br> Используется в ApiController, чтобы прочитать только заголовок jrpc запроса.
 * <br> - узнать имя метода
 * <br> (Не читая параметр запроса, т.к. десериализацией params занимается конкретный обработчик)
 */
@JsonIgnoreProperties(ignoreUnknown = true) // param не десериализуем
public class JrpcRequestHeader extends JrpcBase {

    protected String method;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}

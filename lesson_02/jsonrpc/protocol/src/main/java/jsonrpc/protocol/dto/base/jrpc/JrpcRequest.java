package jsonrpc.protocol.dto.base.jrpc;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;


/**
 *  JrpcRequest, contains jrpc header and jrpc param
 *  <br> По факту, сейчас используется только в тестах.
 *  <br> С помощью объекта этого класса можно сгенерить json для документации api jrpc
 */
//@Component
// proxyMode = ScopedProxyMode.TARGET_CLASS - при каждом вызове к-л метода бина будет создавать new instance
//@Scope("prototype")
public class JrpcRequest extends JrpcRequestHeader {

    private JsonNode params;

    public JsonNode getParams() {
        return params;
    }

    public void setParams(JsonNode param) {
        this.params = param;
    }
}

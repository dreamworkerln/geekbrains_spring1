package jsonrpc.protocol.dto.base.jrpc;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


/**
 *  JrpcRequest, contains jrpc header and jrpc param
 *  <br> По факту, сейчас используется только в тестах.
 *  <br> С помощью объекта этого класса можно сгенерить json для документации api jsonrpc
 */
@Component
@Scope("prototype")
public class JrpcRequest extends JrpcRequestHeader {

    private AbstractDto params;

    public AbstractDto getParams() {
        return params;
    }

    public void setParams(AbstractDto param) {
        this.params = param;
    }
}

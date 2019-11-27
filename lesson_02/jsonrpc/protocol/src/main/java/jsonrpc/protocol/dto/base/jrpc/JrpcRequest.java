package jsonrpc.protocol.dto.base.jrpc;

import jsonrpc.protocol.dto.base.param.AbstractParamDto;
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

    private AbstractParamDto params;

    public AbstractParamDto getParams() {
        return params;
    }

    public void setParams(AbstractParamDto param) {
        this.params = param;
    }
}

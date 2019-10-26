package jsonrpc.protocol.dto.base.jrpc;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class JrpcRequest extends JrpcRequestHeader {

    private JrpcParameter params;

    public JrpcParameter getParams() {
        return params;
    }

    public void setParams(JrpcParameter param) {
        this.params = param;
    }
}

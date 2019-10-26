package jsonrpc.protocol.dto.base.param;

import jsonrpc.protocol.dto.base.jrpc.JrpcParameter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class GetById extends JrpcParameter {
    private Long id;

    protected GetById() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

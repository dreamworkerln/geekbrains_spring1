package jsonrpc.protocol.dto.base.param;

import jsonrpc.protocol.dto.base.jrpc.AbstractDto;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class GetByIdDto extends AbstractDto {
    private Long id;

    protected GetByIdDto() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

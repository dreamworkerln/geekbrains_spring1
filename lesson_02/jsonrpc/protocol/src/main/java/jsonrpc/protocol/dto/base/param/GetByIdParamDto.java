package jsonrpc.protocol.dto.base.param;

import jsonrpc.protocol.dto.base.jrpc.AbstractDto;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class GetByIdParamDto extends AbstractParamDto {

    public static String METHOD_NAME = "getById";

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

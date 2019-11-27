package jsonrpc.protocol.dto.base.param;

import jsonrpc.protocol.dto.base.jrpc.AbstractDto;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Component
@Scope("prototype")
@Validated
public class IdDto extends AbstractDto {

    @NotBlank
    @Min(0)
    private Long id;

    public IdDto(){}

    public IdDto(@Min(0) long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(@Min(0) long id) {
        this.id = id;
    }
}

package jsonrpc.protocol.dto.base.param;

import jsonrpc.protocol.dto.base.jrpc.AbstractDto;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Component
@Scope("prototype")
public class IdDto extends AbstractDto {

    private Long id;

    public IdDto(){}

    public IdDto(long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public static void validate(IdDto idDto) {

        if (idDto == null) {
            throw new IllegalArgumentException("IdDto.id == null");
        }

        if (idDto.id == null || idDto.id <0) {
            throw new IllegalArgumentException("Iid == null < 0");
        }

    }
}

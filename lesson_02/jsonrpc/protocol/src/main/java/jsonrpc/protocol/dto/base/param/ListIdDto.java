package jsonrpc.protocol.dto.base.param;

import jsonrpc.protocol.dto.base.jrpc.AbstractDto;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class ListIdDto extends AbstractDto {

    private List<Long> list = new ArrayList<>();

    public List<Long> getList() {
        return list;
    }

    public void setList(List<Long> list) {
        this.list = list;
    }

    public static void validate(ListIdDto listIdDto) {

        if (listIdDto == null) {
            throw new IllegalArgumentException("longListDto == null");
        }

        if (listIdDto.list == null) {
            throw new IllegalArgumentException("longListDto.list == null");
        }


        listIdDto.list.forEach(l -> {

            if (l == null) {
                throw new IllegalArgumentException("longListDto.list contains null element");
            }
        });

    }
}

package jsonrpc.server.entities.base.param;

import jsonrpc.server.entities.base.AbstractEntity;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class GetById extends AbstractEntity {
    private Long id;

    protected GetById() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static void validate(GetById request) {

        if (request == null) {
            throw new IllegalArgumentException("Error parsing request: params == null");
        }

        if (request.getId() <= 0) {
            throw new IllegalArgumentException("Error parsing request: id <= 0");
        }
    }
}

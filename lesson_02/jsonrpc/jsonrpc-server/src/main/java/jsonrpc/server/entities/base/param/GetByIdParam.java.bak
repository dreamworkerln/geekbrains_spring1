package jsonrpc.server.entities.base.param;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Min;

@Component
@Scope("prototype")
public class GetByIdParam extends AbstractParam {


    // ToDo: оставить валидацию одну из двух
    @Min(0)
    private Long id;

    protected GetByIdParam() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    // =================================================================================

    @Override
    public void validate() {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Error parsing request: id: " + id);
        }
    }
}

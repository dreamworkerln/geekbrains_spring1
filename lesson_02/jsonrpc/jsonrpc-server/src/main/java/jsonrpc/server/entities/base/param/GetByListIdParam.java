package jsonrpc.server.entities.base.param;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class GetByListIdParam extends AbstractParam {

    protected List<Long> idList = new ArrayList<>();

    public List<Long> getIdList() {
        return idList;
    }

    public void setIdList(List<Long> idList) {
        this.idList = idList;
    }

    @Override
    protected void validate() {

        if (idList == null || idList.size() == 0) {
            throw new IllegalArgumentException("Invalid idList");
        }
    }
}

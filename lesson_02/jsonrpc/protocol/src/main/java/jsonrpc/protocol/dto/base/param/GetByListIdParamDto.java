package jsonrpc.protocol.dto.base.param;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class GetByListIdParamDto extends AbstractParamDto{

    public static String METHOD_NAME = "getByListId";

    private List<Long> idList = new ArrayList<>();

    public List<Long> getIdList() {
        return idList;
    }

    public void setIdList(List<Long> idList) {
        this.idList = idList;
    }
}

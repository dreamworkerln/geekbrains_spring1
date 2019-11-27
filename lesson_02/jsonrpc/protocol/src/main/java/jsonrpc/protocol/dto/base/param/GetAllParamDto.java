package jsonrpc.protocol.dto.base.param;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class GetAllParamDto extends AbstractParamDto {

    public static String METHOD_NAME = "getAll";
}

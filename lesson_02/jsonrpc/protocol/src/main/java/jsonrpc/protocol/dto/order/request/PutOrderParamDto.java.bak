package jsonrpc.protocol.dto.order.request;


import jsonrpc.protocol.dto.base.param.AbstractParamDto;
import jsonrpc.protocol.dto.order.OrderDto;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class PutOrderParamDto extends AbstractParamDto {

    public static String METHOD_NAME = "put";

    private OrderDto order;

    public OrderDto getOrder() {
        return order;
    }

    public void setOrder(OrderDto order) {
        this.order = order;
    }
}

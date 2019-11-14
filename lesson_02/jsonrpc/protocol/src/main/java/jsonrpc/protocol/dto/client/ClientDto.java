package jsonrpc.protocol.dto.client;

import jsonrpc.protocol.dto.order.OrderDto;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class ClientDto {
    private List<OrderDto> orderList = new ArrayList<>();

    public List<OrderDto> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<OrderDto> orderList) {
        this.orderList = orderList;
    }
}

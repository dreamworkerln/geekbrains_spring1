package jsonrpc.server.entities.order.request;

import jsonrpc.server.entities.base.param.AbstractParam;
import jsonrpc.server.entities.order.Order;
import jsonrpc.server.entities.order.OrderItem;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class PutOrderParam extends AbstractParam {

    private Order order;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    protected void validate() {

        if (order.getItemList() == null ||
            order.getItemList().size() == 0) {

            throw new IllegalArgumentException("Error parsing request ");
        }
    }
}

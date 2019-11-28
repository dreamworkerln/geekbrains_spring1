package jsonrpc.protocol.dto.order;

import jsonrpc.protocol.dto.base.jrpc.AbstractDto;
import jsonrpc.protocol.dto.base.jrpc.AbstractDtoPersisted;
import jsonrpc.protocol.dto.product.ProductItemDto;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope("prototype")
public class OrderItemDto extends ProductItemDto {

// не надо нам циклических зависимостей
// OrderDto -> OrderDto.itemList -> OrderItemDto -> OrderDto -> ...
//
//    private OrderDto order;
//
//    public OrderDto getOrder() {
//        return order;
//    }
//
//    public void setOrder(OrderDto order) {
//        this.order = order;
//    }


    public static void validate(OrderItemDto productDto) {

        if (productDto == null) {
            throw new IllegalArgumentException("productDto == null");
        }
    }

}

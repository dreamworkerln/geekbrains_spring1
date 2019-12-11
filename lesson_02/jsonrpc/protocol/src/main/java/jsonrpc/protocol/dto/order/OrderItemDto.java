package jsonrpc.protocol.dto.order;

import jsonrpc.protocol.dto.product.ProductItemDto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


//@Component
//@Scope("prototype")
public class OrderItemDto extends ProductItemDto {

    public OrderItemDto() {
    }

    public OrderItemDto(@NotNull Long productId, @NotNull @Min(0) Integer count) {
        super(productId, count);
    }

    // не надо нам циклических зависимостей
    // OrderDto -> OrderDto.itemList -> OrderItemDto -> OrderDto -> ...
    // OrderItemDto не содержит ссылки на OrderDto



    @Override
    public String toString() {
        return "OrderItemDto{" +
               "id=" + id +
               ", productId=" + productId +
               ", count=" + count +
               ", created=" + created +
               ", updated=" + updated +
               '}';
    }
}

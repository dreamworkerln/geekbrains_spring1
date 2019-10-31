package jsonrpc.protocol.dto.order;

import jsonrpc.protocol.dto.Product.ProductDto;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class OrderItemDto {

    private ProductDto product;

    private Integer count;

    public ProductDto getProduct() {
        return product;
    }

    public void setProduct(ProductDto product) {
        this.product = product;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

//    public OrderDto getOrder() {
//        return order;
//    }
//
//    public void setOrder(OrderDto order) {
//        this.order = order;
//    }
}

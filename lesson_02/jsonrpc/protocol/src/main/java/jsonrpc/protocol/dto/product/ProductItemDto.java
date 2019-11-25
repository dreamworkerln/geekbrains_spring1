package jsonrpc.protocol.dto.product;

import jsonrpc.protocol.dto.base.jrpc.AbstractDtoPersisted;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class ProductItemDto extends AbstractDtoPersisted {

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
}

package jsonrpc.protocol.dto.storage;

import jsonrpc.protocol.dto.base.jrpc.AbstractDtoPersisted;
import jsonrpc.protocol.dto.order.OrderItemDto;
import jsonrpc.protocol.dto.product.ProductItemDto;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class StorageDto extends AbstractDtoPersisted {

    private List<ProductItemDto> productList = new ArrayList<>();


    public List<ProductItemDto> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductItemDto> productList) {
        this.productList = productList;
    }
}

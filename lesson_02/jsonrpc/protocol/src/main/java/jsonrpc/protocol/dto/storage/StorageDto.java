package jsonrpc.protocol.dto.storage;

import jsonrpc.protocol.dto.base.AbstractDto;
import jsonrpc.protocol.dto.product.ProductItemDto;

import java.util.ArrayList;
import java.util.List;

//@Component
//@Scope("prototype")
public class StorageDto extends AbstractDto {

    private List<ProductItemDto> productList = new ArrayList<>();

    public List<ProductItemDto> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductItemDto> productList) {
        this.productList = productList;
    }
}

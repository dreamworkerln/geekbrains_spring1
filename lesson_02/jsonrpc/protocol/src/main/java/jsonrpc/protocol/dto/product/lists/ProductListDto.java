package jsonrpc.protocol.dto.product.lists;

import jsonrpc.protocol.dto.base.jrpc.AbstractDto;
import jsonrpc.protocol.dto.product.ProductDto;

import java.util.ArrayList;
import java.util.List;

public class ProductListDto extends AbstractDto {

    private List<ProductDto> productList = new ArrayList<>();

    public List<ProductDto> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductDto> productList) {
        this.productList = productList;
    }
}

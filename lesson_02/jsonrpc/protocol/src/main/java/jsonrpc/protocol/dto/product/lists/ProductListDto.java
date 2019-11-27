package jsonrpc.protocol.dto.product.lists;

import jsonrpc.protocol.dto.base.jrpc.AbstractDto;
import jsonrpc.protocol.dto.product.ProductDto;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


/**
 * Просто обертка над List<ProductDto> <br>
 * Т.к. API требует возвращать объект, а не коллекцию
 */
@Component
@Scope("prototype")
public class ProductListDto extends AbstractDto {

    private List<ProductDto> productList = new ArrayList<>();

    public ProductListDto() {}

    public ProductListDto(List<ProductDto> productList) {
        this.productList = productList;
    }

    public List<ProductDto> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductDto> productList) {
        this.productList = productList;
    }
}

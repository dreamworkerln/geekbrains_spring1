package jsonrpc.protocol.dto.product.lists;

import jsonrpc.protocol.dto.base.jrpc.AbstractDto;
import jsonrpc.protocol.dto.product.ProductItemDto;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Просто обертка над List<ProductItemDto> <br>
 * Т.к. API требует возвращать объект, а не коллекцию
 */
@Component
@Scope("prototype")
public class ProductItemListDto extends AbstractDto {

    private List<ProductItemDto> list = new ArrayList<>();

    public List<ProductItemDto> getList() {
        return list;
    }

    public void setList(List<ProductItemDto> list) {
        this.list = list;
    }

    public ProductItemListDto() {}

    public ProductItemListDto(List<ProductItemDto> list) {
        this.list = list;
    }
}

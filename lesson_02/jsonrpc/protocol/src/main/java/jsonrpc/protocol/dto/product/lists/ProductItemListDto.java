package jsonrpc.protocol.dto.product.lists;

import jsonrpc.protocol.dto.base.jrpc.AbstractDto;
import jsonrpc.protocol.dto.product.ProductItemDto;

import java.util.ArrayList;
import java.util.List;

public class ProductItemListDto extends AbstractDto {

    private List<ProductItemDto> list = new ArrayList<>();

    public List<ProductItemDto> getList() {
        return list;
    }

    public void setList(List<ProductItemDto> list) {
        this.list = list;
    }
}

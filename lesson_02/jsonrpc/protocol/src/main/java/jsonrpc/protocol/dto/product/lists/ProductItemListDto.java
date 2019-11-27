package jsonrpc.protocol.dto.product.lists;

import jsonrpc.protocol.dto.product.ProductItemDto;

import java.util.ArrayList;
import java.util.List;

public class ProductItemListDto {

    private List<ProductItemDto> list = new ArrayList<>();

    public List<ProductItemDto> getList() {
        return list;
    }

    public void setList(List<ProductItemDto> list) {
        this.list = list;
    }
}

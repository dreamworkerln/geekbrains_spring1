package jsonrpc.server.entities.product.lists;

import jsonrpc.server.entities.base.AbstractEntity;
import jsonrpc.server.entities.product.ProductItem;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class ProductItemList extends AbstractEntity {

    private List<ProductItem> list = new ArrayList<>();

    public List<ProductItem> getList() {
        return list;
    }

    public void setList(List<ProductItem> list) {
        this.list = list;
    }
}

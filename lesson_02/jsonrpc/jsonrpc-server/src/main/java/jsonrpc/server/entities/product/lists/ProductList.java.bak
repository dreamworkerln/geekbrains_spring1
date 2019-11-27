package jsonrpc.server.entities.product.lists;

import jsonrpc.server.entities.base.AbstractEntity;
import jsonrpc.server.entities.product.Product;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class ProductList extends AbstractEntity {

    private List<Product> productList = new ArrayList<>();

    public ProductList() {}

    public ProductList(List<Product> productList) {
        this.productList = productList;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}

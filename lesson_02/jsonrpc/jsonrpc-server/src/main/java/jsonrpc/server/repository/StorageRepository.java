package jsonrpc.server.repository;

import jsonrpc.server.entities.product.Product;
import jsonrpc.server.entities.product.ProductItem;

import java.util.List;

public interface StorageRepository {

    ProductItem getById(Long id);
    void add(Product product, int count);
    void remove(Product product, int count);
    List<ProductItem> getAll();
}



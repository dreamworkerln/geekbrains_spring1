package jsonrpc.server.repository;

import jsonrpc.server.entities.product.Product;
import jsonrpc.server.entities.product.ProductItem;

import java.util.List;

public interface StorageRepository {

    ProductItem getById(Long id);
    List<ProductItem> getByListId(List<Long> list);
    List<ProductItem> getAll();
    void put(Product product, int count);        // добавить на склад количество товара
    void remove(Product product, int count);     // забрать со склада количество товара
    void delete(Product product);                // удалить со склада вообще такую позиию
}



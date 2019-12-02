package jsonrpc.server.repository;

import jsonrpc.server.entities.product.Product;
import jsonrpc.server.entities.product.ProductItem;
import jsonrpc.server.entities.storage.StorageItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StorageRepository extends JpaRepository<StorageItem, Long> {

    Optional<ProductItem> findByProductId(Long id);

    void deleteByProductId(Long id);

//    ProductItem getById(Long id);
//    List<ProductItem> getByIdList(List<Long> list);
//    List<ProductItem> getAll();
//    Long save(Product product, int count);        // добавить на склад количество товара
//    Long remove(Product product, int count);     // забрать со склада количество товара
//    void delete(Product product);                // удалить со склада вообще такую позиию
}



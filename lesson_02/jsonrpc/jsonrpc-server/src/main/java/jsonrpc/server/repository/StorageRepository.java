package jsonrpc.server.repository;

import jsonrpc.server.entities.product.Product;
import jsonrpc.server.entities.product.ProductItem;
import jsonrpc.server.entities.storage.StorageItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;

import java.util.List;
import java.util.Optional;

@Transactional
public interface StorageRepository extends JpaRepository<StorageItem, Long> {

// Disabled due to ambiguous naming without mention about locking
//    Optional<ProductItem> findByProductId(Long id);

    //Optional<StorageItem> findById(Long id);

    Optional<StorageItem> findByProductId(Long id);


    @Query("FROM StorageItem item " +
           "INNER JOIN FETCH item.product p " +
           "WHERE p.id IN :idList")    
    List<StorageItem> findAllByProductIdList(@Param("idList")List<Long> idList);

    List<StorageItem> findAll();

    void deleteByProductId(Long id);



    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("FROM StorageItem item " +
           "INNER JOIN FETCH item.product p " +
           "WHERE p.id = :#{#product.id}")
    Optional<StorageItem> lockByProduct(@Param("product")Product product);

//    ProductItem getById(Long id);
//    List<ProductItem> getByIdList(List<Long> list);
//    List<ProductItem> getAll();
//    Long save(Product product, int count);        // добавить на склад количество товара
//    Long remove(Product product, int count);      // забрать со склада количество товара
//    void delete(Product product);                // удалить со склада вообще такую позиию
}



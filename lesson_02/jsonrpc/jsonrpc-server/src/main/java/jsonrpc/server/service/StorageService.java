package jsonrpc.server.service;

import jsonrpc.server.entities.product.Product;
import jsonrpc.server.entities.product.ProductItem;
import jsonrpc.server.entities.storage.StorageItem;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface StorageService extends AbstractService {

    //@Override
    //Optional<ProductItem> findById(Long id);
    
    //Optional<? extends ProductItem> findByProductId(Long id);
    Optional<? extends ProductItem> findByProductId(Long id);

    List<ProductItem> findAllByProductId(List<Long> listId);

    List<ProductItem> findAll();

    /**
     * Положить товар на склад
     */
    void put(@NotNull Product product, int count);


    /**
     * Забрать товар со склада
     */
    void remove(@NotNull Product product, int count);


    /**
     * Завести новый тип товара на складе
     */
    ProductItem save(ProductItem productItem);

    /**
     * Завести новый тип товара на складе
     */
    StorageItem save(StorageItem storageItem);

    /**
     * Удалить тип товара со склада по продукту
     */
    void delete(@NotNull Product product);
    void delete(@NotNull Long productId);



    //
    void validate(ProductItem product);
}

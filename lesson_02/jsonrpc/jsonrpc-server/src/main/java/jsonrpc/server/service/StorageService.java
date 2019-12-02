package jsonrpc.server.service;

import jsonrpc.server.entities.product.Product;
import jsonrpc.server.entities.product.ProductItem;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface StorageService {


    Optional<? extends ProductItem> findById(Long id);

    List<ProductItem> findAllById(List<Long> listId);

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
    Long save(ProductItem productItem);

    /**
     * Удалить тип товара со склада по продукту
     */
    void delete(@NotNull Product product);
    void delete(@NotNull Long productId);



    //
    void validate(ProductItem product);
}

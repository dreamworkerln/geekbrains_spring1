package jsonrpc.server.service;

import jsonrpc.server.entities.product.Product;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface ProductService /*extends AbstractService*/ {

    Optional<Product> findById(Long id);
    List<Product> findAllById(List<Long> listId);
    List<Product> findAll();
    List<Product> findAll(Specification<Product> spec);


    Product save(Product product);

    void delete(Product product);

    //void validate(Product product);

}

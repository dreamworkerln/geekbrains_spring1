package jsonrpc.server.repository;

import jsonrpc.server.entities.product.Product;

import java.util.List;

// public interface OrderRepository extends JpaRepository<product, Long> {}
// emulation
public interface ProductRepository {

    void add(Product product);
    Product getById(Long id);
    List<Product> getAll();
    void delete(Long id);
}

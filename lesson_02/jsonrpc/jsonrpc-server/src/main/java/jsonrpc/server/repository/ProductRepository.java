package jsonrpc.server.repository;

import jsonrpc.server.entities.product.Product;

// public interface OrderRepository extends JpaRepository<Product, Long> {}
// emulation
public interface ProductRepository {

    Product getById(Long id);
}

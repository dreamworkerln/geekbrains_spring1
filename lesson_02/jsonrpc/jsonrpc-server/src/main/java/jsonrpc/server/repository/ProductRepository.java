package jsonrpc.server.repository;

import jsonrpc.server.entities.Product;

// public interface OrderRepository extends JpaRepository<Product, Long> {}
// emulation
public interface ProductRepository {

    Product getById(Long id);
}

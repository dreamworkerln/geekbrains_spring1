package jsonrpc.server.repository;

import jsonrpc.server.entities.Order;


// public interface OrderRepository extends JpaRepository<order, Long> {}

// emulation
public interface OrderRepository {
    Order getById(Long id);
}

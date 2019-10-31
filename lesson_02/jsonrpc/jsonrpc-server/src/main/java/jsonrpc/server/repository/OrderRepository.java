package jsonrpc.server.repository;

import jsonrpc.server.entities.order.Order;


// public interface OrderRepository extends JpaRepository<Order, Long> {}
// emulation
public interface OrderRepository {
    Order getById(Long id);
}

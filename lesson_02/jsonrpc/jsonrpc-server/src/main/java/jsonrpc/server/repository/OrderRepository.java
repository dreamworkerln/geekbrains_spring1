package jsonrpc.server.repository;

import jsonrpc.server.entities.Client;
import jsonrpc.server.entities.Manager;
import jsonrpc.server.entities.order.Order;
import jsonrpc.server.entities.order.OrderItem;
import jsonrpc.server.entities.product.Product;

import java.util.List;


// public interface OrderRepository extends JpaRepository<Order, Long> {}
// emulation
public interface OrderRepository {

    void put(Order order); // return persisted Order (with id, created, updated if was transient)
    Order getById(Long id);
    void delete(Long id);
}

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

    Long put(Order order);
    Order getById(Long id);
    void delete(Long id);
}

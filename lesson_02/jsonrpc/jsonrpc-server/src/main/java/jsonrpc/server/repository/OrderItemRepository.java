package jsonrpc.server.repository;

import jsonrpc.server.entities.order.OrderItem;
import jsonrpc.server.repository.base.CustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends CustomRepository<OrderItem, Long> {
}

package jsonrpc.resourceserver.repository;

import jsonrpc.resourceserver.entities.order.OrderItem;
import jsonrpc.resourceserver.repository.base.CustomRepository;

public interface OrderItemRepository extends CustomRepository<OrderItem, Long> {
}

package jsonrpc.server.service;

import com.fasterxml.jackson.databind.JsonNode;
import jsonrpc.server.entities.order.Order;
import jsonrpc.server.entities.order.OrderItem;
import jsonrpc.server.entities.storage.StorageItem;

import java.util.List;
import java.util.Optional;

public interface OrderService /*extends AbstractService*/ {

    Optional<Order> findById(Long id);

    List<Order> findAllById(List<Long> listId);

    List<Order> findAll();

    Order save(Order order);

    void delete(Order order);

    Optional<OrderItem> findItemById(Long id);
}

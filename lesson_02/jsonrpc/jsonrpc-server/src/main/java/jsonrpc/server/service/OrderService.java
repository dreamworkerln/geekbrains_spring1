package jsonrpc.server.service;

import jsonrpc.server.entities.order.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    Optional<Order> findById(Long id);

    List<Order> findAllById(List<Long> listId);

    List<Order> findAll();

    Long save(Order order);

    void delete(Order order);

    void validate(Order order);
}

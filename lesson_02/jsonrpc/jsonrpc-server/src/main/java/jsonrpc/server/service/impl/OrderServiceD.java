package jsonrpc.server.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jsonrpc.protocol.dto.order.OrderDto;
import jsonrpc.server.entities.order.OrderItem;
import jsonrpc.server.entities.order.mappers.OrderMapper;
import jsonrpc.server.repository.OrderItemRepository;
import jsonrpc.server.repository.OrderRepository;
import jsonrpc.server.service.OrderService;
import jsonrpc.server.entities.order.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.*;


@Service
@Transactional
public class OrderServiceD implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Autowired
    public OrderServiceD(OrderRepository orderRepository,
                         OrderItemRepository orderItemRepository) {

        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }


    // ----------------------------------------------------------------------------------


    @Override
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public List<Order> findAllById(List<Long> listId) {

        return orderRepository.findAllById(listId);
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }



    @Override
    public Order save(Order order) {

        return orderRepository.save(order);
    }

    @Override
    public void delete(Order order) {

        orderRepository.delete(order);
    }

    // ------------------------------------------------------------------------------

    public Optional<OrderItem> findItemById(Long id) {
        return orderItemRepository.findById(id);
    }


    // ================================================================================



//    /**
//     * Обновляет Order.update
//     * Если был изменен к-л из Order.itemList (или добавлен/удален)
//     */
//    private List<Instant> getUList(Order order) {
//
//        List<Instant> result = new ArrayList<>();
//
//        if (order!= null) {
//            order.getItemList().forEach(oi -> result.add(oi.getUpdated()));
//        }
//        return result;
//    }

}

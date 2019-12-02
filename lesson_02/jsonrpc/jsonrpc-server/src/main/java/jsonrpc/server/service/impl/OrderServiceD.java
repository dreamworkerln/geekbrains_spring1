package jsonrpc.server.service.impl;

import jsonrpc.server.entities.storage.StorageItem;
import jsonrpc.server.repository.OrderRepository;
import jsonrpc.server.service.OrderService;
import jsonrpc.server.entities.order.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.time.Instant;
import java.util.*;


@Service
@Transactional
public class OrderServiceD implements OrderService {

    private final OrderRepository orderRepository;
    //private final ProductService productService;
    private final Validator validator;


    public OrderServiceD(OrderRepository orderRepository,
                         Validator validator) {

        this.orderRepository = orderRepository;
        //this.productService = productService;
        this.validator = validator;
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

    // ----------------------------------------------------------------------------



    @Override
    public void validate(Order order) {

        Set<ConstraintViolation<Order>> violations = validator.validate(order);
        if (violations.size() != 0) {
            throw new ConstraintViolationException("Order validation failed", violations);
        }
    }


    // ------------------------------------------------------------------------------

    /**
     * Обновляет Order.update
     * Если был изменен к-л из Order.itemList (или добавлен/удален)
     */
    private List<Instant> getUList(Order order) {

        List<Instant> result = new ArrayList<>();

        if (order!= null) {
            order.getItemList().forEach(oi -> result.add(oi.getUpdated()));
        }
        return result;
    }

}

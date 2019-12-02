package jsonrpc.server.service.impl;

import jsonrpc.server.entities.product.Product;
import jsonrpc.server.repository.OrderRepository;
import jsonrpc.server.service.OrderService;
import jsonrpc.server.entities.order.Order;
import jsonrpc.server.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
@Transactional
public class OrderServiceDefault implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final Validator validator;

    public OrderServiceDefault(OrderRepository orderRepository, ProductService productService, Validator validator) {
        this.orderRepository = orderRepository;
        this.productService = productService;
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
    public Long save(Order order) {

        // Fetch all persisted Products
        order.getItemList().forEach(item -> {

            Product p = item.getProduct();
            Product persistedProduct = productService.findById(p.getId()).orElse(null);

            if (persistedProduct == null) {
                throw new IllegalArgumentException("Order Product not persisted");
            }
            item.setProduct(persistedProduct);
            item.setOrder(order);  // Set owner to all OrderItems
        });

        orderRepository.save(order);
        orderRepository.toUpdate(order, Instant.EPOCH);

        return order.getId();
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

}

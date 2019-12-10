package jsonrpc.server.service;

import jsonrpc.protocol.dto.order.OrderDto;
import jsonrpc.server.entities.order.OrderItem;
import jsonrpc.server.repository.OrderItemRepository;
import jsonrpc.server.entities.order.Order;
import jsonrpc.server.repository.OrderRepository;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final StorageService storageService;

    @Autowired
    public OrderService(OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository, StorageService storageService) {

        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;

        this.storageService = storageService;
    }


    // ----------------------------------------------------------------------------------


    public Optional<Order> findById(Long id) {

        return orderRepository.findById(id);
    }

    public List<Order> findAllById(List<Long> listId) {

        return orderRepository.findAllById(listId);
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Order save(Order order) {

        Order result = null;

        // Защита
        // Если это новый заказ, то у него статус должен быть QUEUED (нет резервирования товара)
        // т.к. SELECT FOR UPDATE требует существующую строку в БД
        if (order.getId() == null) {
            order.setStatus(OrderDto.Status.QUEUED);

            // создаем заказ со статусом QUEUED
            result = orderRepository.save(order);

            try {

                // SELECT FOR UPDATE блокируем строки в Order, Product, OrderItem, StorageItem
                orderRepository.lockByOrder(order);

                orderRepository.lockByOrder(order);

                // пытаемся зарезарвировать со склада каждый товар из элементов заказа
                order.getItemList().forEach(oi -> storageService.remove(oi.getProduct(), oi.getCount()));

            }
            catch(Exception e) {
                // Ну если в delete() упали, что поделаешь ...
                orderRepository.delete(order);

                // Re-throw upper причину невозможности создания заказа
                // (бросание исключения в нормальном режиме - на складе недостаточно товара для выполнения заказа)
                throw e;
            }
        }
        else {
            throw new NotImplementedException("Редактировать существующий заказ моя пока не уметь - " +
                                              "это надо со склада товары заново перерезервировать.");
        }






        orderRepository.refresh(result);

        return result;
    }

//
//    public Order saveAndFlush(Order order) {
//
//        Order result = refreshableRepository.saveAndFlush(order);
//        refreshableRepository.refresh(order);
//        return result;
//    }





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

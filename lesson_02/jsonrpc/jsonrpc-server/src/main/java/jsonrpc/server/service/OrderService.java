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



            // SELECT FOR UPDATE блокируем строки в Order, Product, OrderItem, StorageItem
            // блокировки, как я понял - reentrant, на своем lock ты повторно не вешаешься
            // если работаешь в одной транзакции(или в продленнйо транзакции)
            orderRepository.lockByOrder(order);

            // пытаемся зарезервировать со склада каждый товар из элементов заказа
            order.getItemList().forEach(oi -> storageService.remove(oi.getProduct(), oi.getCount()));

            // Если товара на складе будет не достаточно, транзакция откатится и заказ не создастся

        }
        else {

            // блокируемся нах
            orderRepository.lockByOrder(order);

            // берем старый заказ
            Order oldOrder = orderRepository.findById(order.getId()).orElseThrow(() ->
                    new IllegalArgumentException("Order not exists:\n" + order.getId()));

            // возвращаем все взад как было до этого заказа
            // (пока дельты не будем считать по товарам)
            oldOrder.getItemList().forEach(oi -> storageService.put(oi.getProduct(), oi.getCount()));

            // начинаем заново
            result = orderRepository.save(order);

            // пытаемся зарезервировать со склада каждый товар из элементов заказа
            order.getItemList().forEach(oi -> storageService.remove(oi.getProduct(), oi.getCount()));

        }


        // Обновляем заказ(подгружаем все дочерние элементы, чтоб не юзался кеш хибера)
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

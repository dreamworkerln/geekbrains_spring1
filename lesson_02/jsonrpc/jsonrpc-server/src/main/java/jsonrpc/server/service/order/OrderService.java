package jsonrpc.server.service.order;

import jsonrpc.protocol.dto.order.OrderDto;
import jsonrpc.server.entities.order.OrderItem;
import jsonrpc.server.repository.OrderItemRepository;
import jsonrpc.server.entities.order.Order;
import jsonrpc.server.repository.OrderRepository;
import jsonrpc.server.service.InvalidLogicException;
import jsonrpc.server.service.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;


@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final StorageService storageService;
    private final OrderLogic orderLogic;

    @Autowired
    public OrderService(OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository, StorageService storageService, OrderLogic orderLogic) {

        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;

        this.storageService = storageService;
        this.orderLogic = orderLogic;
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

        Long orderId = order.getId();
        
        Order old;

        if (order.getId() == null) {
            // фикс-защита
            order.setStatus(OrderDto.Status.ORDERED);

            // заглушка для логики
            old = new Order();
            old.setStatus(OrderDto.Status.NULL);
        }
        else {
            // берем старый заказ
            old = orderRepository.findById(order.getId()).orElseThrow(() ->
                    new ValidationException("OrderN not exists:\n" + orderId));
        }

        // Списки действий
        List<OrderAct> createAct     = Collections.singletonList(orderLogic::create);
        List<OrderAct> editAct       = Collections.singletonList(orderLogic::edit);
        List<OrderAct> deliverAct    = Collections.singletonList(orderLogic::deliver);
        List<OrderAct> cancelAct     = Collections.singletonList(orderLogic::cancel);
        List<OrderAct> cancelBackAct = Arrays.asList(orderLogic::cancel, orderLogic::back);
        List<OrderAct> completeAct   = Collections.singletonList(orderLogic::complete);

        // строим матрицу решений (квадратная)

        int mSize = OrderDto.Status.values().length;
        @SuppressWarnings("unchecked")
        List<OrderAct>[][] m = (List<OrderAct>[][])Array.newInstance(List.class, mSize, mSize);


        int NUL = OrderDto.Status.NULL.ordinal();
        int ORD = OrderDto.Status.ORDERED.ordinal();
        int TRA = OrderDto.Status.TRANSIT.ordinal();
        int CAN = OrderDto.Status.CANCELED.ordinal();
        int COM = OrderDto.Status.COMPLETED.ordinal();


        //OLD  NEW    ACTION LIST
        m[NUL][ORD] = createAct;
        m[ORD][ORD] = editAct;
        m[ORD][TRA] = deliverAct;
        m[ORD][CAN] = cancelAct;
        m[TRA][CAN] = cancelBackAct;
        m[TRA][COM] = completeAct;


        // смотрим, что у нас тут
        int oldOrd = old.getStatus().ordinal();
        int newOrd = order.getStatus().ordinal();


        // выполняем логику жизнненого цикла заказа ...
        if (m[oldOrd][newOrd] != null) {
            // поочередно применяем назначенные дествия к заказу
            for (OrderAct act :  m[oldOrd][newOrd]) {
                order = act.apply(order);
            }
        }
        else {
            // Такая логика запрещена
            throw new InvalidLogicException("Invalid OrderN operation: " +
                                            "from " + old.getStatus() + " to " + order.getStatus());
        }

        // Обновляем заказ(подгружаем все дочерние элементы, чтоб не юзался кеш хибера)
        // orderRepository.refresh(order);

        return order;
    }

//
//    public OrderN saveAndFlush(OrderN order) {
//
//        OrderN result = refreshableRepository.saveAndFlush(order);
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
//     * Обновляет OrderN.update
//     * Если был изменен к-л из OrderN.itemList (или добавлен/удален)
//     */
//    private List<Instant> getUList(OrderN order) {
//
//        List<Instant> result = new ArrayList<>();
//
//        if (order!= null) {
//            order.getItemList().forEach(oi -> result.add(oi.getUpdated()));
//        }
//        return result;
//    }

    // alias
    interface OrderAct extends Function<Order,Order>{}

}

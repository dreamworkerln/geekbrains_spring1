package jsonrpc.server.service.order;

import jsonrpc.server.entities.order.Order;
import jsonrpc.server.repository.OrderRepository;
import jsonrpc.server.service.StorageService;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;


@Service
@Transactional
public class OrderLogic {

    private final OrderRepository orderRepository;
    private final StorageService storageService;

    public OrderLogic(OrderRepository orderRepository, StorageService storageService) {
        this.orderRepository = orderRepository;
        this.storageService = storageService;
    }





    public Order create(Order order) {

        // для надежности
        // order.setStatus(OrderDto.Status.ORDERED);

        // SELECT FOR UPDATE требует существующую строку в БД
        order = orderRepository.save(order);

        // подгружаем все связанные с заказом элементы графа
        orderRepository.refresh(order);


        // SELECT FOR UPDATE блокируем строки в Order, Product, OrderItem, StorageItem
        // блокировки, как я понял - reentrant, на своем lock ты повторно не вешаешься
        // если работаешь в одной транзакции(или в продленной транзакции)
        orderRepository.lockByOrder(order);


        // пытаемся зарезервировать со склада каждый товар из элементов заказа
        // Если товара на складе будет не достаточно, транзакция откатится и заказ не создастся
        order.getItemList().forEach(oi -> storageService.remove(oi.getProduct(), oi.getCount()));

        return order;
    }




    public Order edit(Order order) {

        // блокируемся нах
        orderRepository.lockByOrder(order);

        // берем старый заказ (по 2 разу, возьмет из кеша, т.к. одна транзакция)
        Long orderId = order.getId();
        Order old = orderRepository.findById(order.getId()).orElseThrow(() ->
                new ValidationException("Order not exists:\n" + orderId));

        // возвращаем все взад как было до этого заказа
        // (пока дельты не будем считать по товарам)
        old.getItemList().forEach(oi -> storageService.put(oi.getProduct(), oi.getCount()));

        // начинаем заново
        order = orderRepository.save(order);

        // подгружаем все связанные с заказом элементы графа
        orderRepository.refresh(order);

        // пытаемся зарезервировать со склада каждый товар из элементов заказа
        order.getItemList().forEach(oi -> storageService.remove(oi.getProduct(), oi.getCount()));

        return order;

    }



    public Order deliver(Order order) {
        throw new NotImplementedException("ПЫЩЬ-ПЫЩЬ поехали к клиенту ...");

    }




    public Order cancel(Order order) {
        throw new NotImplementedException("ЗАКАЗ ОТМЕНЕН");

    }



    public Order back(Order order) {

        throw new NotImplementedException("ПЫЩЬ-ПЫЩЬ поехали обратно на склад ...");
    }



    public Order complete(Order order) {
        throw new NotImplementedException("ПОКУПАТЕЛИ РАДЫ");
    }


}

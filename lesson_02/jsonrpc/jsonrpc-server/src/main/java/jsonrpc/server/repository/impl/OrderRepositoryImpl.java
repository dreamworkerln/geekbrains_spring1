package jsonrpc.server.repository.impl;


import com.github.javafaker.Faker;
import jsonrpc.server.entities.order.Order;
import jsonrpc.server.entities.order.OrderItem;
import jsonrpc.server.entities.product.Product;
import jsonrpc.server.repository.OrderRepository;
import jsonrpc.server.utils.Utils;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;

import static jsonrpc.server.utils.Utils.idSetter;
import static jsonrpc.server.utils.Utils.toLong;

// Fake repository

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    // In-memory DB emulation
    private ConcurrentMap<Long, Order> orderList = new ConcurrentSkipListMap<>();

    private AtomicLong identity = new AtomicLong(1);
    private AtomicLong oiIdentity = new AtomicLong(1);


    /**
     * SaveOrUpdate Order
     */
    @Override
    public void put(Order order) {

        // assign id if not persisted yet
        if (order.getId() == null) {
            Utils.idSetter(order, identity.getAndIncrement());
            order.toCreate();
        }
        order.toUpdate();


        // тут дрочь, проще грохнуть весь заказ и переоформить заново,
        // чем пытаться определить  - какой из элементов заказа
        // поменяли а какой нет, пускай этим hibernate занимается
        // (хоть также перезаписывет все элементы заказа)
        //
        // Пускай не редактируют заказы задним числом .....
        for (OrderItem oi : order.getItemList()) {

            if (oi.getId() == null) {
                Utils.idSetter(oi, oiIdentity.getAndIncrement());
                oi.toCreate();
            }
            oi.toUpdate();
        }

        // Adding clone not to allow to mess up
        orderList.put(order.getId(), Order.clone(order));
    }

    @Override
    public Order getById(Long id) {

        return Order.clone(orderList.get(id));
    }

    @Override
    public void delete(Long id) {
        throw new NotImplementedException("ProductRepositoryImpl.delete() not supported");
    }

    //private Faker faker = new Faker();

//    private final ProductRepository productRepository;
//
//    @Autowired
//    public OrderRepositoryImpl(ProductRepository productRepository) {
//        this.productRepository = productRepository;
//    }


//    @Override
//    public Order getById(Long id) {
//
//        Order result = null;
//
//        //if (id == 33) {
//            // emulation
//            result = new Order();
//
//            // init create and update time
//            result.toCreate();
//            result.toUpdate();
//
//            // set id via reflection
//            idSetter(result, id);
//
//            // Just unassociated list of random created products
//            for (int i = 0; i < faker.number().numberBetween(1, 5); i++) {
//
//                product p = productRepository.getById(toLong(faker.number().digits(7)));
//
//                OrderItem oi = new OrderItem(p, faker.number().numberBetween(1, 50));
//                oi.toCreate();
//                oi.toUpdate();
//                idSetter(oi, toLong(faker.number().digits(7)));
//
//                //idSetter(oi, toLong(faker.number().digits(7)));
//                result.addItem(oi);
//            }
//        //}
//
//        return result;
//    }
}

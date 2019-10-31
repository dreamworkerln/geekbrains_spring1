package jsonrpc.server.repository;


import com.github.javafaker.Faker;
import jsonrpc.server.entities.order.Order;
import jsonrpc.server.entities.order.OrderItem;
import jsonrpc.server.entities.Product;
import jsonrpc.server.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.Instant;

import static jsonrpc.server.utils.Utils.idSetter;
import static jsonrpc.server.utils.Utils.toLong;

// Fake repository

@Component
@Primary
public class OrderRepositoryImpl implements OrderRepository{

    private Faker faker = new Faker();

    private final ProductRepository productRepository;

    @Autowired
    public OrderRepositoryImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Order getById(Long id) {

        Order result = null;

        if (id == 33) {
            // emulation
            result = new Order();
            result.setDate(Instant.ofEpochSecond(1572105211));

            result.toCreate();
            result.toUpdate();


            idSetter(result, 33L);

            // Just unassociated list of random created products
            for (int i = 0; i < faker.number().numberBetween(1, 5); i++) {

                Product p = productRepository.getById(toLong(faker.number().digits(7)));

                OrderItem oi = new OrderItem(p, i);
                Utils.idSetter(oi, toLong(faker.number().digits(7)));
                oi.setCount(faker.number().numberBetween(1, 50));
                result.addItem(oi);
            }

//            for (int i = 0; i < faker.number().numberBetween(1, 5); i++) {
//                result.getZololo().add(faker.cat().name());
//            }
        }

        return result;
    }
}

package jsonrpc.server.repository;


import com.github.javafaker.Faker;
import jsonrpc.server.entities.order.Order;
import jsonrpc.server.entities.order.OrderItem;
import jsonrpc.server.entities.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static jsonrpc.server.utils.Utils.idSetter;
import static jsonrpc.server.utils.Utils.toLong;

// Fake repository

@Service
//@Primary
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

            // init create and update time
            result.toCreate();
            result.toUpdate();

            // set id via reflection
            idSetter(result, 33L);

            // Just unassociated list of random created products
            for (int i = 0; i < faker.number().numberBetween(1, 5); i++) {

                Product p = productRepository.getById(toLong(faker.number().digits(7)));

                OrderItem oi = new OrderItem(p, faker.number().numberBetween(1, 50));
                idSetter(oi, toLong(faker.number().digits(7)));
                oi.toCreate();
                oi.toUpdate();

                //idSetter(oi, toLong(faker.number().digits(7)));
                result.addItem(oi);
            }
        }

        return result;
    }
}

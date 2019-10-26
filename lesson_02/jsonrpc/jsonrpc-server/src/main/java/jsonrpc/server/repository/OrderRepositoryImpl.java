package jsonrpc.server.repository;


import jsonrpc.server.entities.Order;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@Primary
public class OrderRepositoryImpl implements OrderRepository{

    @Override
    public Order getById(Long id) {

        Order result = null;

        if (id == 33) {
            // emulation
            result = new Order();
            result.setId(id);
            result.setDate(Instant.ofEpochSecond(1572105211));
        }

        return result;
    }
}

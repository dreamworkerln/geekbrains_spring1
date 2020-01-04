package jsonrpc.resourceserver.entities.order.mappers;

import jsonrpc.protocol.dto.order.OrderDto;
import jsonrpc.resourceserver.entities.base.mapper.AbstractConverter;
import jsonrpc.resourceserver.entities.order.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.validation.ValidationException;

@Service
public class OrderConverter extends AbstractConverter<Order,OrderDto, Void> {

    private final OrderMapper orderMapper;

    @Autowired
    OrderConverter(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }


    @PostConstruct
    void postConstruct() {
        // Autowire вручную
        setMappers(orderMapper::toEntity,null,orderMapper::toDto,null);
        //setJsonToDto(o -> objectMapper.treeToValue(o, OrderDto.class),null);
        setClasses(Order.class, OrderDto.class, null);
    }

    @Override
    protected void validate(Order order) {
        super.validate(order);

        // "Нельзя пускать на сохранение OrderN у которого id=null, но есть элементы от другого order'a с ненулевыми id"
        if (order.getId() == null) {
            
            order.getItemList().forEach(item -> {
                if (item.getId() != null) {
                    throw new ValidationException("New OrderN referenced to existing OrderItem from other OrderN");
                }
            });
        }
    }
}
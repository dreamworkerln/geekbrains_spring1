package jsonrpc.server.entities.order.mappers;

import jsonrpc.protocol.dto.order.OrderDto;
import jsonrpc.server.entities.base.mapper.AbstractConverter;
import jsonrpc.server.entities.order.Order;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

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
        setJsonToDto(o -> objectMapper.treeToValue(o, OrderDto.class),null);
    }

    @Override
    protected void validate(Order entity) {
        super.validate(entity);

        throw new NotImplementedException("Нельзя пускать на сохранение Order у которого id=null, но есть элементы от другого ordera с ненулевыми id");


        // InvalidDataAccessApiUsageException: detached entity passed to persist: jsonrpc.server.entities.order.OrderItem;
    }
}
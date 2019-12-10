package jsonrpc.server.entities.order.mappers;

import jsonrpc.protocol.dto.order.OrderDto;
import jsonrpc.server.controller.jrpc.base.AbstractConverterZ;
import jsonrpc.server.entities.order.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class OrderConverter extends AbstractConverterZ<Order,OrderDto, Void> {

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
}
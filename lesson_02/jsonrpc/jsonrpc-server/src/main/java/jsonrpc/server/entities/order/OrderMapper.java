package jsonrpc.server.entities.order;

import jsonrpc.protocol.dto.base.jrpc.AbstractDto;
import jsonrpc.protocol.dto.order.OrderDto;
import jsonrpc.server.entities.base.AbstractEntity;
import jsonrpc.server.entities.base.mapper.AbstractMapper;
import org.modelmapper.ExpressionMap;
import org.modelmapper.TypeMap;
import org.modelmapper.builder.ConfigurableConditionExpression;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Instant;

@Component
public class OrderMapper extends AbstractMapper<Order, OrderDto> {

    OrderMapper() {
        super(Order.class, OrderDto.class);
    }

    @PostConstruct
    protected void setupMapper() {

        //

        TypeMap<Order, OrderDto> typeMap =

                mapper.createTypeMap(Order.class, OrderDto.class).addMappings(
                        // назначаем маппинг полей (для final типов map полей нельзя указывать - не поддерживается блевотекой)
                        // соответственно отключаем маппинг для полей, которые не совпадают
                        mapper -> {
                            //mapper.skip(OrderDto::setDate);
                            // такие типы (поля) конвертируются потом отдельно в пост-конвертере
                        }).setPostConverter(getToDtoConverter());




        mapper.createTypeMap(OrderDto.class, Order.class).addMappings(
                mapper -> {
                    //mapper.skip(Order::setDate);
                }).setPostConverter(getToEntityConverter());




    }

    @Override
    protected void mapSpecificFields(Order source, OrderDto destination) {
        //destination.setDate(source.getDate().getEpochSecond());
    }

    @Override
    protected void mapSpecificFields(OrderDto  source, Order destination) {
        //destination.setDate(Instant.ofEpochSecond(source.getDate()));
    }


}

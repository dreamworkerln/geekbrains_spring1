package jsonrpc.server.entities.order;

import jsonrpc.protocol.dto.order.OrderItemDto;
import jsonrpc.server.entities.base.mapper.AbstractMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class OrderItemMapper extends AbstractMapper<OrderItem, OrderItemDto> {

    OrderItemMapper() {
        super(OrderItem.class, OrderItemDto.class);
    }

    @PostConstruct
    protected void setupMapper() {

        TypeMap<OrderItem, OrderItemDto> tmEtD =

                mapper.createTypeMap(OrderItem.class, OrderItemDto.class).addMappings(
                        // назначаем маппинг полей (для final типов map полей нельзя указывать - не поддерживается блевотекой)
                        // соответственно отключаем маппинг для полей, которые не совпадают по типам
                        mapper -> {

                            mapper.skip(OrderItemDto::setCount);

                            //mapper.skip(OrderItemDto::setCreated);

                            //mapper.skip(OrderDto::setDate);
                            // такие типы (поля) конвертируются потом отдельно в пост-конвертере, конкретно тут - в mapSpecificFields
                        }).setPostConverter(getToDtoConverter());



        TypeMap<OrderItemDto, OrderItem> tmDtE = mapper.createTypeMap(OrderItemDto.class, OrderItem.class).addMappings(
                mapper -> {
                    //mapper.skip(Order::setDate);
                }).setPostConverter(getToEntityConverter());


        includeBase(tmEtD, tmDtE);
    }

    @Override
    protected void mapSpecificFields(OrderItem source, OrderItemDto destination) {
        // пост-конвертор у предка не вызывается(?), поэтому стартуем с толкача
        super.mapSpecificFields(source, destination); // Не забудь вызывать этот метод!

        destination.setCount(3);
    }

    @Override
    protected void mapSpecificFields(OrderItemDto source, OrderItem destination) {
        super.mapSpecificFields(destination, source);

    }


}

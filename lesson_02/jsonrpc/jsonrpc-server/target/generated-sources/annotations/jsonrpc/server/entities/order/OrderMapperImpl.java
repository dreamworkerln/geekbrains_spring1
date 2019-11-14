package jsonrpc.server.entities.order;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import jsonrpc.protocol.dto.order.OrderDto;
import jsonrpc.protocol.dto.order.OrderItemDto;
import jsonrpc.server.entities.base.mapper.InstantLongMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-11-14T19:09:31+0300",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 1.8.0_222 (Private Build)"
)
@Component
public class OrderMapperImpl implements OrderMapper {

    @Autowired
    private InstantLongMapper instantLongMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;

    @Override
    public OrderDto toDto(Order order) {
        if ( order == null ) {
            return null;
        }

        OrderDto orderDto = new OrderDto();

        orderDto.setId( order.getId() );
        orderDto.setCreated( instantLongMapper.map( order.getCreated() ) );
        orderDto.setUpdated( instantLongMapper.map( order.getUpdated() ) );
        orderDto.setItemList( orderItemListToOrderItemDtoList( order.getItemList() ) );
        orderDto.setClient( OrderMapper.map( order.getClient() ) );
        orderDto.setManager( OrderMapper.map( order.getManager() ) );

        return orderDto;
    }

    @Override
    public Order toEntity(OrderDto orderDto) {
        if ( orderDto == null ) {
            return null;
        }

        Order order = new Order();

        order.setCreated( instantLongMapper.map( orderDto.getCreated() ) );
        order.setUpdated( instantLongMapper.map( orderDto.getUpdated() ) );
        order.setItemList( orderItemDtoListToOrderItemList( orderDto.getItemList() ) );
        order.setClient( OrderMapper.map( orderDto.getClient() ) );
        order.setManager( OrderMapper.map( orderDto.getManager() ) );

        return order;
    }

    protected List<OrderItemDto> orderItemListToOrderItemDtoList(List<OrderItem> list) {
        if ( list == null ) {
            return null;
        }

        List<OrderItemDto> list1 = new ArrayList<OrderItemDto>( list.size() );
        for ( OrderItem orderItem : list ) {
            list1.add( orderItemMapper.toDto( orderItem ) );
        }

        return list1;
    }

    protected List<OrderItem> orderItemDtoListToOrderItemList(List<OrderItemDto> list) {
        if ( list == null ) {
            return null;
        }

        List<OrderItem> list1 = new ArrayList<OrderItem>( list.size() );
        for ( OrderItemDto orderItemDto : list ) {
            list1.add( orderItemMapper.toEntity( orderItemDto ) );
        }

        return list1;
    }
}

package jsonrpc.server.entities.order;

import javax.annotation.Generated;
import jsonrpc.protocol.dto.order.OrderItemDto;
import jsonrpc.server.entities.base.mapper.InstantLongMapper;
import jsonrpc.server.entities.product.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-11-14T19:09:31+0300",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 1.8.0_222 (Private Build)"
)
@Component
public class OrderItemMapperImpl implements OrderItemMapper {

    @Autowired
    private InstantLongMapper instantLongMapper;
    @Autowired
    private ProductMapper productMapper;

    @Override
    public OrderItemDto toDto(OrderItem orderItem) {
        if ( orderItem == null ) {
            return null;
        }

        OrderItemDto orderItemDto = new OrderItemDto();

        orderItemDto.setId( orderItem.getId() );
        orderItemDto.setCreated( instantLongMapper.map( orderItem.getCreated() ) );
        orderItemDto.setUpdated( instantLongMapper.map( orderItem.getUpdated() ) );
        orderItemDto.setProduct( productMapper.toDto( orderItem.getProduct() ) );
        orderItemDto.setCount( orderItem.getCount() );

        return orderItemDto;
    }

    @Override
    public OrderItem toEntity(OrderItemDto orderItemDto) {
        if ( orderItemDto == null ) {
            return null;
        }

        OrderItem orderItem = new OrderItem();

        orderItem.setCreated( instantLongMapper.map( orderItemDto.getCreated() ) );
        orderItem.setUpdated( instantLongMapper.map( orderItemDto.getUpdated() ) );
        orderItem.setProduct( productMapper.toEntity( orderItemDto.getProduct() ) );
        orderItem.setCount( orderItemDto.getCount() );

        return orderItem;
    }
}

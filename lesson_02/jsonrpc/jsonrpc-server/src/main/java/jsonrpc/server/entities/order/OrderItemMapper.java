package jsonrpc.server.entities.order;

import jsonrpc.protocol.dto.order.OrderItemDto;
import jsonrpc.server.entities.base.mapper.InstantLongMapper;
import jsonrpc.server.entities.product.ProductMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        //unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {InstantLongMapper.class, ProductMapper.class})
public interface OrderItemMapper {

    OrderItemDto toDto(OrderItem orderItem);
    OrderItem toEntity(OrderItemDto orderItemDto);
}

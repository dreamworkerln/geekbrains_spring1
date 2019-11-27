package jsonrpc.server.entities.order.request;



import jsonrpc.protocol.dto.order.request.PutOrderParamDto;
import jsonrpc.server.entities.base.mapper.InstantLongMapper;
import jsonrpc.server.entities.order.OrderMapper;
import jsonrpc.server.entities.product.ProductItemMapper;
import jsonrpc.server.entities.product.ProductMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        //unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {InstantLongMapper.class, OrderMapper.class})
        // OrderMapper.class, OrderItemMapper.class, ProductMapper.class, ProductItemMapper.class

public interface PutOrderMapper {

    PutOrderParamDto toDto(PutOrderParam putOrderParam);
    PutOrderParam toEntity(PutOrderParamDto putOrderParamDto);
}

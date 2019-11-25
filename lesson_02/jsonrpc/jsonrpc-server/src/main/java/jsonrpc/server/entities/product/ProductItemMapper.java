package jsonrpc.server.entities.product;

import jsonrpc.protocol.dto.product.ProductItemDto;
import jsonrpc.server.entities.base.mapper.InstantLongMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        //unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {InstantLongMapper.class, ProductMapper.class})
public interface ProductItemMapper {

    ProductItemDto toDto(ProductItem productItem);
    ProductItem toEntity(ProductItemDto productItemDto);
}

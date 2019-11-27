package jsonrpc.server.entities.product.lists;

import jsonrpc.protocol.dto.product.lists.ProductItemListDto;
import jsonrpc.server.entities.base.mapper.InstantLongMapper;
import jsonrpc.server.entities.product.ProductItemMapper;
import jsonrpc.server.entities.product.ProductMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {InstantLongMapper.class, ProductMapper.class, ProductItemMapper.class})
public interface ProductItemListMapper {

    ProductItemListDto toDto(ProductItemList productItem);
    ProductItemList toEntity(ProductItemListDto productItemDto);
}

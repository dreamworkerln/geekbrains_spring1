package jsonrpc.server.entities.product.mappers;

import jsonrpc.protocol.dto.product.ProductItemDto;
import jsonrpc.server.entities.base.mapper.InstantMapper;
import jsonrpc.server.entities.product.ProductItem;
import jsonrpc.server.entities.storage.StorageItem;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = {InstantMapper.class, ProductMapper.class, ProductItemMapper.class})
public interface ProductItemListMapper {

    List<ProductItemDto> toDto(List<ProductItem> itemList);
    List<ProductItem> toEntity(List<ProductItemDto> itemDtoList);
}

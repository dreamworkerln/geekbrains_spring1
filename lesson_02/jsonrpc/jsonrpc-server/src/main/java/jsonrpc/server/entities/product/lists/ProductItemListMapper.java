package jsonrpc.server.entities.product.lists;

import jsonrpc.protocol.dto.product.ProductItemDto;
import jsonrpc.server.entities.base.mapper.InstantMapper;
import jsonrpc.server.entities.product.ProductItem;
import jsonrpc.server.entities.product.ProductItemMapper;
import jsonrpc.server.entities.product.ProductMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = {InstantMapper.class, ProductMapper.class, ProductItemMapper.class})
public interface ProductItemListMapper {

    List<ProductItemDto> toDto(List<ProductItem> productItem);
    List<ProductItem> toEntity(List<ProductItemDto> productItemDto);
}

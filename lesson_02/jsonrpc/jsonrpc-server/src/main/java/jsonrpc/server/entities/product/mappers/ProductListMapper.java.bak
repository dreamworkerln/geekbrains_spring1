package jsonrpc.server.entities.product.mappers;


import jsonrpc.protocol.dto.product.ProductDto;
import jsonrpc.server.entities.base.mapper.InstantMapper;
import jsonrpc.server.entities.product.Product;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = {InstantMapper.class, ProductMapper.class})
public interface ProductListMapper {

    List<ProductDto> toDto(List<Product> productList);
    List<Product> toEntity(List<ProductDto> productListDto);
}

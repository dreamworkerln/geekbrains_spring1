package jsonrpc.server.entities.product.lists;


import jsonrpc.protocol.dto.product.lists.ProductListDto;
import jsonrpc.server.entities.base.mapper.InstantLongMapper;
import jsonrpc.server.entities.product.ProductMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = {InstantLongMapper.class, ProductMapper.class})
public interface ProductListMapper {

    ProductListDto toDto(ProductList productList);
    ProductList toEntity(ProductListDto productListDto);
}

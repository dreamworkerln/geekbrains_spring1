package jsonrpc.server.entities.product;

import jsonrpc.protocol.dto.product.ProductDto;
import jsonrpc.protocol.dto.product.ProductItemDto;
import jsonrpc.server.entities.base.mapper.InstantLongMapper;
import jsonrpc.server.utils.Utils;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = {InstantLongMapper.class})
public interface ProductItemMapper {

    @Mapping(source = "product", target = "productId", qualifiedByName = "toProductDto")
    ProductItemDto toDto(ProductItem productItem);

    @Mapping(source = "productId", target = "product", qualifiedByName = "toProduct")
    ProductItem toEntity(ProductItemDto productItemDto);


    default Long toProductDto(Product product) {
        return product.getId();
    }

    default Product toProduct(Long productId) {
        Product result = new Product();
        Utils.idSetter(result, productId);
        return result;
    }


//    // Map Product -> ProductDto (required mapping): Product ->  productId
//    @AfterMapping
//    default void setId(ProductItem source, @MappingTarget ProductItemDto target) {
//        target.setProductId(source.getId());
//    }

}

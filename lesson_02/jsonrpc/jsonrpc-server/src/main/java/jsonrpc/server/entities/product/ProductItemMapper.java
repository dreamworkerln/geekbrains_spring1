package jsonrpc.server.entities.product;

import jsonrpc.protocol.dto.order.OrderDto;
import jsonrpc.protocol.dto.product.ProductDto;
import jsonrpc.protocol.dto.product.ProductItemDto;
import jsonrpc.server.entities.base.mapper.InstantLongMapper;
import jsonrpc.server.entities.order.Order;
import jsonrpc.server.utils.Utils;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = {InstantLongMapper.class, ProductMapper.class})
public interface ProductItemMapper {

    @Mapping(source = "product", target = "productId", qualifiedByName = "toProductDto")
    ProductItemDto toDto(ProductItem productItem);

    @Mapping(source = "productId", target = "product", qualifiedByName = "toProduct")
    ProductItem toEntity(ProductItemDto productItemDto);

//    // ProductItem.product -> ProductItemDto.productId
//    default Long toProductDto(Product product) {
//        return product.getId();
//    }
//
//    // ProductItemDto.productId -> ProductItem.product
//    default Product toProduct(Long productId) {
//        Product result = new Product();
//        Utils.idSetter(result, productId);
//        return result;
//    }


    // у Product protected setId(), и делать его public я не хочу,
    // а MapStruct не умеет работать через отражения с protected членами
    // (или я не знаю как), поэтому делаем это вручную
    @AfterMapping
    default void setId(ProductItemDto source, @MappingTarget ProductItem target) {
        Utils.idSetter(target, source.getId());
    }


//    // Map Product -> ProductDto (required mapping): Product ->  productId
//    @AfterMapping
//    default void setId(ProductItem source, @MappingTarget ProductItemDto target) {
//        target.setProductId(source.getId());
//    }

}

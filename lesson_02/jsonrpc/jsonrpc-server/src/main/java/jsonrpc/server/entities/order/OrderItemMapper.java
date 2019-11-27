package jsonrpc.server.entities.order;

import jsonrpc.protocol.dto.order.OrderItemDto;
import jsonrpc.server.entities.base.mapper.InstantLongMapper;
import jsonrpc.server.entities.product.ProductMapper;
import jsonrpc.server.utils.Utils;
import org.mapstruct.*;


@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = {InstantLongMapper.class, ProductMapper.class})
public interface OrderItemMapper {

    @Mapping(source = "product", target = "productId", qualifiedByName = "toProductDto")
    OrderItemDto toDto(OrderItem orderItem);


    @Mapping(source = "productId", target = "product", qualifiedByName = "toProduct")
    @Mapping(target = "order", ignore = true)
    OrderItem toEntity(OrderItemDto orderItemDto);


//    default Long toProductDto(Product product) {
//        return product.getId();
//    }
//
//    default Product toProduct(Long productId) {
//        //ToDo: Залезть в ProductRepository, загрузить продукт по id
//        // Если его там нету, то в данном случае - брость исключение
//        // Сделать то же самое для всех методов Mapper.toEntity в остальныз Mapper'ах
//
//        Product result = new Product();
//        Utils.idSetter(result, productId);
//        return result;
//    }

    

//    default Product toProduct(ProductDto productDto) {
//        Product result = new Product();
//        Utils.idSetter(result, productDto.getId());
//        return result;
//    }

//    default ProductDto toProductDto(Product product) {
//        ProductDto result = new ProductDto();
//        result.setId(product.getId());
//        return result;
//    }



    @AfterMapping
    default void setId(OrderItemDto source, @MappingTarget OrderItem target) {
        Utils.idSetter(target, source.getId());
    }
}

package jsonrpc.server.entities.order.mappers;

import jsonrpc.protocol.dto.order.OrderItemDto;
import jsonrpc.server.entities.base.mapper.IdMapper;
import jsonrpc.server.entities.base.mapper.InstantMapper;
import jsonrpc.server.entities.order.OrderItem;
import jsonrpc.server.entities.product.mappers.ProductMapper;
import jsonrpc.server.service.OrderService;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;


@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = {InstantMapper.class, ProductMapper.class})
public abstract class OrderItemMapper implements IdMapper {

    @Autowired
    OrderService orderService;

    @Mapping(source = "product", target = "productId", qualifiedByName = "toProductDto")
    public abstract OrderItemDto toDto(OrderItem orderItem);


    // хрена, в OrderItemDto не будет ссылки на OrderDto, задолбал stackOverflow
    // https://github.com/mapstruct/mapstruct/issues/469
    // https://github.com/mapstruct/mapstruct/pull/911
    // (вроде как mapStruct графы с циклами может как-то разруливать, но хз как)
    @Mapping(source = "productId", target = "product", qualifiedByName = "toProduct")
    @Mapping(target = "order", ignore = true)
    public abstract OrderItem toEntity(OrderItemDto orderItemDto);



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
    void afterMapping(OrderItemDto source, @MappingTarget OrderItem target) {

        idMap(orderService::findItemById, source, target);
    }
}




/*



@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = {InstantMapper.class, ProductMapper.class})
public interface   OrderItemMapper extends AbstractMapper {

    @Mapping(source = "product", target = "productId", qualifiedByName = "toProductDto")
    OrderItemDto toDto(OrderItem orderItem);


    @Mapping(source = "productId", target = "product", qualifiedByName = "toProduct")

    // хрена, в OrderItemDto не будет ссылки на OrderDto, задолбал stackOverflow
    // https://github.com/mapstruct/mapstruct/issues/469
    // https://github.com/mapstruct/mapstruct/pull/911
    // (вроде как mapStruct графы с циклами может как-то разруливать, но хз как)
    @Mapping(target = "order", ignore = true)
    OrderItem toEntity(OrderItemDto orderItemDto);



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

        // 1. set id, created, updated
        postMap(orderService, source, target);

        // Do not map created & updated - only server produce this values
    }
}
 */
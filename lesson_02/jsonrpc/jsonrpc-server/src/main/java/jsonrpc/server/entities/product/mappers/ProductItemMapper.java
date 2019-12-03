package jsonrpc.server.entities.product.mappers;

import jsonrpc.protocol.dto.product.ProductItemDto;
import jsonrpc.server.entities.base.mapper.IdMapper;
import jsonrpc.server.entities.base.mapper.InstantMapper;
import jsonrpc.server.entities.product.ProductItem;
import jsonrpc.server.service.StorageService;
import jsonrpc.utils.Utils;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = {InstantMapper.class, ProductMapper.class})
public abstract class ProductItemMapper implements IdMapper {

    @Autowired
    private StorageService storageService;

    @Mapping(source = "product", target = "productId", qualifiedByName = "toProductDto")
    public abstract ProductItemDto toDto(ProductItem productItem);

    @Mapping(source = "productId", target = "product", qualifiedByName = "toProduct")
    public abstract ProductItem toEntity(ProductItemDto productItemDto);

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
    void afterMapping(ProductItemDto source, @MappingTarget ProductItem target) {
        idMap(storageService::findById, source, target);
    }


//    // Map Product -> ProductDto (required mapping): Product ->  productId
//    @AfterMapping
//    default void setId(ProductItem source, @MappingTarget ProductItemDto target) {
//        target.setProductId(source.getId());
//    }

}

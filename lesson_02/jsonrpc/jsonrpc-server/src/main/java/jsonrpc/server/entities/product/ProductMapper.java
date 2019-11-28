package jsonrpc.server.entities.product;

import jsonrpc.protocol.dto.product.ProductDto;
import jsonrpc.server.entities.base.mapper.InstantMapper;
import jsonrpc.utils.Utils;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = {InstantMapper.class})
public interface ProductMapper {

    //ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    //@Mapping(source = "person.description", target = "description")
    //@Mapping(source = "address.houseNo", target = "houseNumber")
    ProductDto toDto(Product product);
    Product toEntity(ProductDto productDto);


    default Long toProductDto(Product product) {
        return product.getId();
    }

    default Product toProduct(Long productId) {

        Product result = new Product();
        Utils.idSetter(result, productId);
        return result;
    }


    // у Product protected setId(), и делать его public я не хочу,
    // а MapStruct не умеет работать через отражения с protected членами
    // (или я не знаю как), поэтому делаем это вручную
    @AfterMapping
    default void setId(ProductDto source, @MappingTarget Product target) {
        Utils.idSetter(target, source.getId());
    }

}




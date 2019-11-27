package jsonrpc.server.entities.product;

import jsonrpc.protocol.dto.product.ProductDto;
import jsonrpc.server.entities.base.mapper.InstantLongMapper;
import jsonrpc.server.utils.Utils;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = {InstantLongMapper.class})
public interface ProductMapper {

    //ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    //@Mapping(source = "person.description", target = "description")
    //@Mapping(source = "address.houseNo", target = "houseNumber")
    ProductDto toDto(Product product);
    Product toEntity(ProductDto productDto);

    // у Product protected setId(), и делать его public я не хочу,
    // а MapStruct не умеет работать через отражения с protected членами
    // поэтому делаем это вручную
    @AfterMapping
    default void setId(ProductDto source, @MappingTarget Product target) {
        Utils.idSetter(target, source.getId());
    }

}




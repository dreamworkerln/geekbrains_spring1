package jsonrpc.server.entities.product;

import jsonrpc.protocol.dto.product.ProductDto;
import jsonrpc.server.entities.base.mapper.InstantLongMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        //unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {InstantLongMapper.class})
public interface ProductMapper {

    //ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    //@Mapping(source = "person.description", target = "description")
    //@Mapping(source = "address.houseNo", target = "houseNumber")
    ProductDto toDto(Product product);
    Product toEntity(ProductDto productDto);
}




package jsonrpc.server.entities.product;

import jsonrpc.protocol.dto.Product.ProductDto;
import jsonrpc.server.entities.base.mapper.InstantLongMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {InstantLongMapper.class})
public interface ProductMapper2 {

    ProductMapper2 INSTANCE = Mappers.getMapper(ProductMapper2.class);

    //@Mapping(source = "person.description", target = "description")
    //@Mapping(source = "address.houseNo", target = "houseNumber")
    ProductDto toDto(Product product);
}




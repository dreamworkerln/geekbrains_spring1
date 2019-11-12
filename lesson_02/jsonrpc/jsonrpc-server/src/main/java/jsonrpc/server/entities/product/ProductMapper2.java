package jsonrpc.server.entities.product;

import jsonrpc.protocol.dto.Product.ProductDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper2 {

    //@Mapping(source = "person.description", target = "description")
    //@Mapping(source = "address.houseNo", target = "houseNumber")
    ProductDto toDto(Product source , ProductDto dest);
}




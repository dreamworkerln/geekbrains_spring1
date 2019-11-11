package jsonrpc.server.entities.product;

import jsonrpc.protocol.dto.Product.ProductDto;
import jsonrpc.server.entities.base.mapper.AbstractMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;





@Component
public class ProductMapper extends AbstractMapper<Product, ProductDto> {

    ProductMapper() {
        super(Product.class, ProductDto.class);
    }

    @PostConstruct
    protected void setupMapper() {

        TypeMap<Product, ProductDto> tmEtD =

                mapper.createTypeMap(Product.class, ProductDto.class).addMappings(
                        mapper -> {
                            mapper.skip(ProductDto::setvCode);
                        }).setPostConverter(getToDtoConverter());



        TypeMap<ProductDto, Product> tmDtE =
                mapper.createTypeMap(ProductDto.class, Product.class).addMappings(
                mapper -> {
                }).setPostConverter(getToEntityConverter());

        includeBase(tmEtD, tmDtE);
    }

    @Override
    protected void mapSpecificFields(Product source, ProductDto destination) {
        super.mapSpecificFields(source, destination);

        destination.setvCode("ЗУЗУЗУ!!!");
    }

    @Override
    protected void mapSpecificFields(ProductDto source, Product destination) {
        super.mapSpecificFields(destination, source);
    }
}




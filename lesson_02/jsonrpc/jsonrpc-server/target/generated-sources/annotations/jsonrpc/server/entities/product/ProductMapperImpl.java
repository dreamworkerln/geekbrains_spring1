package jsonrpc.server.entities.product;

import javax.annotation.Generated;
import jsonrpc.protocol.dto.Product.ProductDto;
import jsonrpc.server.entities.base.mapper.InstantLongMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-11-14T14:44:08+0300",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 1.8.0_222 (Private Build)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Autowired
    private InstantLongMapper instantLongMapper;

    @Override
    public ProductDto toDto(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductDto productDto = new ProductDto();

        productDto.setId( product.getId() );
        productDto.setCreated( instantLongMapper.map( product.getCreated() ) );
        productDto.setUpdated( instantLongMapper.map( product.getUpdated() ) );
        productDto.setName( product.getName() );
        productDto.setvCode( product.getvCode() );

        return productDto;
    }

    @Override
    public Product toEntity(ProductDto productDto) {
        if ( productDto == null ) {
            return null;
        }

        Product product = new Product();

        product.setCreated( instantLongMapper.map( productDto.getCreated() ) );
        product.setUpdated( instantLongMapper.map( productDto.getUpdated() ) );
        product.setName( productDto.getName() );
        product.setvCode( productDto.getvCode() );

        return product;
    }
}

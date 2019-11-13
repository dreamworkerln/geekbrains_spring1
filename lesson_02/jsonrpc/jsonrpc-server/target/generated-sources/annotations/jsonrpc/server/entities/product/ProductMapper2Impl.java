package jsonrpc.server.entities.product;

import javax.annotation.Generated;
import jsonrpc.protocol.dto.Product.ProductDto;
import jsonrpc.server.entities.base.mapper.InstantLongMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-11-13T19:45:17+0300",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 1.8.0_222 (Private Build)"
)
@Component
public class ProductMapper2Impl implements ProductMapper2 {

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
}

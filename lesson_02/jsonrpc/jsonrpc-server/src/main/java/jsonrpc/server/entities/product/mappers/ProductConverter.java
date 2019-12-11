package jsonrpc.server.entities.product.mappers;

import jsonrpc.protocol.dto.base.filter.specification.ProductSpecDto;
import jsonrpc.protocol.dto.product.ProductDto;
import jsonrpc.server.entities.base.mapper.AbstractConverter;
import jsonrpc.server.entities.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class ProductConverter extends AbstractConverter<Product,ProductDto, ProductSpecDto> {

    private final ProductMapper productMapper;

    @Autowired
    ProductConverter(ProductMapper orderMapper) {
        this.productMapper = orderMapper;
    }


    @PostConstruct
    void postConstruct() {
        // Autowire вручную
        setMappers(productMapper::toEntity,productMapper::toEntityList,
                productMapper::toDto,productMapper::toDtoList);

        setJsonToDto(o -> objectMapper.treeToValue(o, ProductDto.class),
                o -> objectMapper.treeToValue(o,ProductSpecDto.class));
    }
}

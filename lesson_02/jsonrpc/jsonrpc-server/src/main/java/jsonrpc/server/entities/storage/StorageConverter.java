package jsonrpc.server.entities.storage;

import jsonrpc.protocol.dto.product.ProductItemDto;
import jsonrpc.server.entities.base.mapper.AbstractConverter;
import jsonrpc.server.entities.product.ProductItem;
import jsonrpc.server.entities.product.mappers.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class StorageConverter extends AbstractConverter<ProductItem,ProductItemDto, Void> {

    private final ProductMapper productMapper;

    @Autowired
    StorageConverter(ProductMapper orderMapper) {
        this.productMapper = orderMapper;
    }


    @PostConstruct
    void postConstruct() {
        // Autowire вручную
        setMappers(productMapper::toItemEntity,productMapper::toItemEntityList,
                productMapper::toItemDto,productMapper::toItemDtoList);

        setJsonToDto(o -> objectMapper.treeToValue(o, ProductItemDto.class),
                null);
    }
}

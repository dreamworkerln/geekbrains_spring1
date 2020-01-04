package jsonrpc.resourceserver.entities.storage;

import jsonrpc.protocol.dto.product.ProductItemDto;
import jsonrpc.resourceserver.entities.base.mapper.AbstractConverter;
import jsonrpc.resourceserver.entities.product.ProductItem;
import jsonrpc.resourceserver.entities.product.mappers.ProductMapper;
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

        setClasses(ProductItem.class, ProductItemDto.class, null);

//        setJsonToDto(o -> objectMapper.treeToValue(o, ProductItemDto.class),
//                null);
    }
}

package jsonrpc.resourceserver.entities.product.mappers;

import jsonrpc.protocol.dto.product.spec.ProductSpecDto;
import jsonrpc.protocol.dto.product.ProductDto;
import jsonrpc.resourceserver.entities.base.mapper.AbstractConverter;
import jsonrpc.resourceserver.entities.product.Product;
import jsonrpc.resourceserver.service.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.validation.ValidationException;

@Service
public class ProductConverter extends AbstractConverter<Product,ProductDto, ProductSpecDto> {

    private final ProductMapper productMapper;
    private final CategoryService categoryService;

    @Autowired
    ProductConverter(ProductMapper orderMapper, CategoryService categoryService) {
        this.productMapper = orderMapper;
        this.categoryService = categoryService;
    }


    @PostConstruct
    void postConstruct() {
        // Autowire вручную
        setMappers(productMapper::toEntity,productMapper::toEntityList,
                productMapper::toDto,productMapper::toDtoList);

        setClasses(Product.class, ProductDto.class, ProductSpecDto.class);

//        setJsonToDto(o -> objectMapper.treeToValue(o, ProductDto.class),
//                o -> objectMapper.treeToValue(o,ProductSpecDto.class));
    }

    @Override
    protected void validate(Product entity) {
        super.validate(entity);

        // Check that category exists
        Long categoryId = entity.getCategory().getId();
        categoryService.findById(categoryId).orElseThrow(() -> new ValidationException("Invalid product.category"));
    }
}

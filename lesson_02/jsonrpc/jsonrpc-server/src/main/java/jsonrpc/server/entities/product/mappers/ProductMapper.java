package jsonrpc.server.entities.product.mappers;

import jsonrpc.protocol.dto.product.ProductDto;
import jsonrpc.server.entities.base.mapper.IdMapper;
import jsonrpc.server.entities.base.mapper.InstantMapper;
import jsonrpc.server.entities.category.Category;
import jsonrpc.server.entities.product.Product;
import jsonrpc.server.service.ProductService;
import jsonrpc.server.service.StorageService;
import jsonrpc.utils.Utils;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = {InstantMapper.class})
public abstract class ProductMapper implements IdMapper {

    @Autowired
    private ProductService productService;

    //ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    //@Mapping(source = "person.description", target = "description")
    //@Mapping(source = "address.houseNo", target = "houseNumber")

    @Mapping(source = "category", target = "categoryId", qualifiedByName = "toCategoryDto")
    public abstract ProductDto toDto(Product product);

    //@Mapping(target = "category", ignore = true)

    @Mapping(source = "categoryId", target = "category", qualifiedByName = "toCategory")
    public abstract Product toEntity(ProductDto productDto);


    public Long toProductDto(Product product) {
        return product.getId();
    }

    public Product toProduct(Long productId) {

        Product result = new Product();
        Utils.fieldSetter("id", result, productId);
        return result;
    }

    public Long toCategoryDto(Category category) {
        return category.getId();
    }

    public Category toCategory(Long categoryId) {

        Category result = new Category();
        Utils.fieldSetter("id", result, categoryId);
        return result;
    }


    // у product protected setId(), и делать его public я не хочу,
    // а MapStruct не умеет работать через отражения с protected членами
    // (или я не знаю как), поэтому делаем это вручную
    @AfterMapping
    void setId(ProductDto source, @MappingTarget Product target) {

        idMap(productService::findById, source, target);
    }

}




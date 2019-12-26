package jsonrpc.resourceserver.entities.product.mappers;

import jsonrpc.protocol.dto.product.ProductDto;
import jsonrpc.protocol.dto.product.ProductItemDto;
import jsonrpc.resourceserver.entities.base.mapper.AbstractMapper;
import jsonrpc.resourceserver.entities.base.mapper.InstantMapper;
import jsonrpc.resourceserver.entities.category.Category;
import jsonrpc.resourceserver.entities.product.Product;
import jsonrpc.resourceserver.entities.product.ProductItem;
import jsonrpc.resourceserver.service.product.ProductService;
import jsonrpc.resourceserver.service.storage.StorageService;
import jsonrpc.utils.Utils;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = {InstantMapper.class /*ProductMapper.ProductFactory.class*/})
public abstract class ProductMapper extends AbstractMapper {

    @Autowired
    private ProductService productService;

    @Autowired
    private StorageService storageService;

    //@Autowired
    //ProductFactory productFactory;

    //ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    //@Mapping(source = "person.description", target = "description")
    //@Mapping(source = "address.houseNo", target = "houseNumber")

    @Mapping(source = "category.id", target = "categoryId")
    public abstract ProductDto toDto(Product product);

    //@Mapping(target = "category", ignore = true)

    //@Mapping(source = "categoryId", target = "category", qualifiedByName = "toCategory")
    //@Mapping(target = "category", ignore = true)
    @Mapping(source = "categoryId", target = "category"/*, qualifiedByName = "toCategory"*/)
    public abstract Product toEntity(ProductDto productDto);

    // ------------------------------------------------------------

    @Mapping(source = "product.id", target = "productId")
    public abstract ProductItemDto toItemDto(ProductItem productItem);


    //@Mapping(target = "product", ignore = true)
    @Mapping(source = "productId", target = "product")
    public abstract ProductItem toItemEntity(ProductItemDto productItemDto);


    public abstract List<ProductDto> toDtoList(List<Product> productList);
    public abstract List<Product> toEntityList(List<ProductDto> productListDto);

    public abstract List<ProductItemDto> toItemDtoList(List<ProductItem> itemList);
    public abstract List<ProductItem> toItemEntityList(List<ProductItemDto> itemDtoList);


    // ============================================================================

    public Product toProduct(Long productId) {

        //ProductN result = productFactory.createProduct();
        Product result = new Product();
        Utils.fieldSetter("id", result, productId);
        return result;
    }


    // ToDo make dedicated controller and mapper to Category
    Category toCategory(Long categoryId) {

        //Category result = productFactory.createCategory();
        Category result = new Category();
        Utils.fieldSetter("id", result, categoryId);
        return result;
    }
    

//    public Category toEntityId(Long dtoId) {
//
//        Category result = new Category();
//        Utils.fieldSetter("id", result, dtoId);
//        return result;
//    }


//    void setEntityId(Long dtoId, @MappingTarget AbstractEntity target) {
//
//        Utils.fieldSetter("id", target, dtoId);
//    }






    // у product protected setId(), и делать его public я не хочу,
    // а MapStruct не умеет работать через отражения с protected членами
    // (или я не знаю как), поэтому делаем это вручную
    @AfterMapping
    void afterMapping(ProductDto source, @MappingTarget Product target) {

        idMap(productService::findById, source, target);
    }


    @AfterMapping
    void afterMapping(ProductItemDto source, @MappingTarget ProductItem target) {
        idMap(storageService::findById, source, target);
    }


    // ====================================================================




    // Заставляем MapStruct не создавать объекты через New(),
    // а обращаться к контексту Spring для создания нового бина через контекст
    // (документация MapStruct "9. Object factories")
    //
    // Пока фабрика не нужна, т.к. Entity и DTO - это не бины
    @Service
    public static class ProductFactory {

        private final ApplicationContext context;

        @Autowired
        public ProductFactory(ApplicationContext context) {
            this.context = context;
        }

        ProductDto createProductDto() {

            return context.getBean(ProductDto.class);
        }

        Product createProduct() {

            return context.getBean(Product.class);
        }

        Category createCategory() {

            return context.getBean(Category.class);
        }

        // Not exists
//        CategoryDto createCategory() {
//
//            return context.getBean(CategoryDto.class);
//        }

        ProductItem createProductItem() {

            return context.getBean(ProductItem.class);
        }

        ProductItemDto createProductItemDto() {

            return context.getBean("productItemDto", ProductItemDto.class);
        }

    }
}




package jsonrpc.server.controller.jrpc.product;


import com.fasterxml.jackson.databind.JsonNode;
import jsonrpc.protocol.dto.base.HandlerName;
import jsonrpc.protocol.dto.base.filter.specification.ProductSpecDto;
import jsonrpc.server.controller.jrpc.base.JrpcMethod;
import jsonrpc.server.entities.product.Product;

import jsonrpc.server.controller.jrpc.base.JrpcController;
import jsonrpc.server.entities.product.mappers.ProductConverter;
import jsonrpc.server.repository.specifications.product.ProductSpecBuilder;
import jsonrpc.server.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;


@Service
@JrpcController(path = HandlerName.Product.path)
public class ProductController {

    private final static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ProductService productService;
    private final ProductConverter converter;

    @Autowired
    public ProductController(
            ProductService productService,
            ProductConverter converter) {

        this.productService = productService;
        this.converter = converter;
    }

    // -----------------------------------------------------------------------------


    @JrpcMethod(method = HandlerName.Product.findById)
    public JsonNode findById(JsonNode params) {

        long id = converter.getId(params);
        Product product = productService.findById(id).orElse(null);
        return converter.toDtoJson(product);
    }




    @JrpcMethod(method = HandlerName.Product.findAllById)
    public JsonNode findAllById(JsonNode params) {

        List<Long> idList = converter.getIdList(params);
        List<Product> list = productService.findAllById(idList);
        return converter.toDtoListJson(list);
    }



    @JrpcMethod(method = HandlerName.Product.findAll)
    public JsonNode findAll(JsonNode params) {

        Optional<ProductSpecDto> specDto = converter.toSpecDto(params);
        Specification<Product> spec =  ProductSpecBuilder.build(specDto);
        return converter.toDtoListJson(productService.findAll(spec));
    }


    /**
     * Return first ProductSpecDto.limit elements
     */
    @JrpcMethod(method = HandlerName.Product.findFirst)
    public JsonNode findFirst(JsonNode params) {

        Optional<ProductSpecDto> specDto = converter.toSpecDto(params);
        Specification<Product> spec = ProductSpecBuilder.build(specDto);

        int limit = specDto.map(ProductSpecDto::getLimit).orElse(1);
        Page<Product> page = productService.findAll(spec, PageRequest.of(0, limit));
        return converter.toDtoListJson(page.toList());
    }


    @JrpcMethod(method = HandlerName.Product.save)
    public JsonNode save(JsonNode params) {

        Product product = converter.toEntity(params);
        product = productService.save(product);
        return converter.toIdJson(product);
    }

    @JrpcMethod(method = HandlerName.Product.delete)
    public JsonNode delete(JsonNode params) {

        Product product = converter.toEntity(params);
        productService.delete(product);
        return null;
    }





    // ==========================================================================================



/*

    @Service
    static class ProductConverter extends AbstractConverter {

        private final ProductMapper productMapper;


        public ProductConverter(ProductMapper productMapper) {

            this.productMapper = productMapper;
        }

        // Dto => Entity
        Product toProduct(JsonNode params)  {
            try {
                ProductDto dto = objectMapper.treeToValue(params, ProductDto.class);
                Product result = productMapper.toEntity(dto);
                validate(result);
                return result;
            }
            // It's request, only IllegalArgumentException - will lead to HTTP 400 ERROR
            catch (Exception e) {
                throw new IllegalArgumentException("Jackson parse error:\n" + e.getMessage(), e);
            }
        }

        // Dto => Entity (ProductSpecDto doesn't have Entity pair, only Dto for now)
        Optional<ProductSpecDto> toSpecDto(JsonNode params) {

            try {
                return Optional.ofNullable(objectMapper.treeToValue(params, ProductSpecDto.class));
                // It's request, only IllegalArgumentException - will lead to HTTP 400 ERROR
            }
            catch (Exception e) {
                throw new IllegalArgumentException("Jackson parse error:\n" + e.getMessage(), e);
            }
        }

        // Entity => Dto
        JsonNode toProductDtoJson(Product product) {
            ProductDto productDto = productMapper.toDto(product);
            return objectMapper.valueToTree(productDto);
        }

        // EntityList => Dto
        JsonNode toProductListDtoJson(List<Product> productList) {
            List<ProductDto> dtoList = productMapper.toDtoList(productList);
            return objectMapper.valueToTree(dtoList);
        }



        void validate(Product product) {

            Set<ConstraintViolation<Product>> violations = validator.validate(product);
            if (violations.size() != 0) {
                throw new ConstraintViolationException("product validation failed", violations);
            }
        }
    }
*/

}


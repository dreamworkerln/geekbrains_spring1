package jsonrpc.server.controller.jrpc.product;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jsonrpc.protocol.dto.base.HandlerName;
import jsonrpc.protocol.dto.product.ProductDto;
import jsonrpc.server.controller.jrpc.base.AbstractConverter;
import jsonrpc.server.controller.jrpc.base.JrpcMethod;
import jsonrpc.server.entities.product.Product;
import jsonrpc.server.entities.product.mappers.ProductListMapper;
import jsonrpc.server.entities.product.mappers.ProductMapper;
import jsonrpc.server.controller.jrpc.base.JrpcController;
import jsonrpc.server.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Set;


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
        return converter.toJsonProductDto(product);
    }




    @JrpcMethod(method = HandlerName.Product.findAllById)
    public JsonNode findAllById(JsonNode params) {

        List<Long> idList = converter.getIdList(params);
        List<Product> list = productService.findAllById(idList);
        return converter.toJsonProductListDto(list);
    }



    @JrpcMethod(method = HandlerName.Product.findAll)
    public JsonNode findAll(JsonNode params) {

        List<Product> list = productService.findAll();
        return converter.toJsonProductListDto(list);
    }
    


    @JrpcMethod(method = HandlerName.Product.save)
    public JsonNode save(JsonNode params) {

        Product product = converter.toProduct(params);
        product = productService.save(product);
        return converter.toJsonId(product);
    }

    @JrpcMethod(method = HandlerName.Product.delete)
    public JsonNode delete(JsonNode params) {

        Product product = converter.toProduct(params);
        productService.delete(product);
        return null;
    }



    // ==========================================================================================


    @Service
    @Transactional
    static class ProductConverter extends AbstractConverter {

        private final ProductMapper productMapper;
        private final ProductListMapper productListMapper;


        public ProductConverter(ObjectMapper objectMapper,
                                Validator validator,
                                ProductMapper productMapper,
                                ProductListMapper productListMapper) {

            super(objectMapper, validator);
            this.productMapper = productMapper;
            this.productListMapper = productListMapper;
        }

        public Product toProduct(JsonNode params)  {
            Product result;
            try {
                ProductDto dto = objectMapper.treeToValue(params, ProductDto.class);
                result = productMapper.toEntity(dto);
                validate(result);
            }
            // It's request, only IllegalArgumentException - will lead to HTTP 400 ERROR
            catch (Exception e) {
                throw new IllegalArgumentException("Jackson parse error:\n" + e.getMessage(), e);
            }

            return result;
        }

        public JsonNode toJsonProductDto(Product product) {
            ProductDto productDto = productMapper.toDto(product);
            return objectMapper.valueToTree(productDto);
        }


        public JsonNode toJsonProductListDto(List<Product> productList) {
            List<ProductDto> listDto = productListMapper.toDto(productList);
            return objectMapper.valueToTree(listDto);
        }


        public JsonNode toJsonId(Product product) {
            return objectMapper.valueToTree(product.getId());
        }


        public void validate(Product product) {

            Set<ConstraintViolation<Product>> violations = validator.validate(product);
            if (violations.size() != 0) {
                throw new ConstraintViolationException("Product validation failed", violations);
            }
        }
    }

}


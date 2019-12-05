package jsonrpc.server.controller.jrpc.product;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.Atomics;
import jsonrpc.protocol.dto.base.HandlerName;
import jsonrpc.protocol.dto.base.filter.specification.ProductSpecDto;
import jsonrpc.protocol.dto.product.ProductDto;
import jsonrpc.server.controller.jrpc.base.AbstractConverter;
import jsonrpc.server.controller.jrpc.base.JrpcMethod;
import jsonrpc.server.entities.product.Product;
import jsonrpc.server.entities.product.mappers.ProductMapper;
import jsonrpc.server.controller.jrpc.base.JrpcController;
import jsonrpc.server.repository.specifications.product.ProductSpecBuilder;
import jsonrpc.server.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;


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

        Optional<ProductSpecDto> specDto = converter.toSpecDto(params);
        Specification<Product> spec =  ProductSpecBuilder.build(specDto);
        return converter.toJsonProductListDto(productService.findAll(spec));

        
        //if (specDtoOp.isPresent()) {

            //ProductSpecDto specDto = specDtoOp.get();
            // Устанавливаем для спецификации имя поля цены
            //specDto.setFieldName(Utils.getPriceFieldName(Product.class, BigDecimal.class));

            //spec = new ProductSpecification(new SpecSearchCriteria("price", SearchOperation.GREATER_THAN, 20));

//            spec = new ProductSpecification(
//                    new SpecSearchCriteria("dodo", SearchOperation.IN, Arrays.asList(0, 1, 2, 3)));
//


            /*
            // Выберется что-то одно
            if (sp.validInterval()) {
                spec = spec.and(priceSpecification.between(specDto.getFieldName(), specDto.getMin(), specDto.getMax()));
            }
            if (sp.validFrom()) {
                spec = spec.and(priceSpecification.from(specDto.getFieldName(), specDto.getMin()));
            }
            if (sp.validTo()) {
                spec = spec.and(priceSpecification.to(specDto.getFieldName(), specDto.getMax()));
            }
            */
        //}

        //List<Product> list = productService.findAll(spec);

        //List<product> list = productService.findAll();

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


        public ProductConverter(ProductMapper productMapper) {

            this.productMapper = productMapper;
        }


        public Product toProduct(JsonNode params)  {
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

        public Optional<ProductSpecDto> toSpecDto(JsonNode params) {

            try {
                return Optional.ofNullable(objectMapper.treeToValue(params, ProductSpecDto.class));
                // It's request, only IllegalArgumentException - will lead to HTTP 400 ERROR
            }
            catch (Exception e) {
                throw new IllegalArgumentException("Jackson parse error:\n" + e.getMessage(), e);
            }
        }


        public JsonNode toJsonProductDto(Product product) {
            ProductDto productDto = productMapper.toDto(product);
            return objectMapper.valueToTree(productDto);
        }


        public JsonNode toJsonProductListDto(List<Product> productList) {
            List<ProductDto> dtoList = productMapper.toDtoList(productList);
            return objectMapper.valueToTree(dtoList);
        }



        public void validate(Product product) {

            Set<ConstraintViolation<Product>> violations = validator.validate(product);
            if (violations.size() != 0) {
                throw new ConstraintViolationException("product validation failed", violations);
            }
        }
    }

}


package jsonrpc.server.controller.jrpc.product;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jsonrpc.protocol.dto.base.HandlerName;
import jsonrpc.protocol.dto.product.ProductDto;
import jsonrpc.server.controller.jrpc.base.JrpcMethod;
import jsonrpc.server.entities.product.Product;
import jsonrpc.server.entities.product.mappers.ProductListMapper;
import jsonrpc.server.entities.product.mappers.ProductMapper;
import jsonrpc.server.controller.jrpc.base.AbstractJrpcController;
import jsonrpc.server.controller.jrpc.base.JrpcController;
import jsonrpc.server.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;


@Service
@JrpcController(path = HandlerName.Product.path)
public class ProductController extends AbstractJrpcController {

    private final static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ProductService productService;
    private final ProductListMapper productListMapper;
    private final ProductMapper productMapper;





    @Autowired
    public ProductController(
            ObjectMapper objectMapper,
            ProductService productService, ProductListMapper productListMapper,
            ProductMapper productMapper) {

        super(objectMapper);

        this.productService = productService;
        this.productListMapper = productListMapper;
        this.productMapper = productMapper;
    }

    // -----------------------------------------------------------------------------


    @JrpcMethod(method = HandlerName.Product.findById)
    public JsonNode findById(JsonNode params) {

        // request id
        long id = getId(params);

        // Getting from service product by id
        Product product = productService.findById(id).orElse(null);
        ProductDto productDto = productMapper.toDto(product);
        return objectMapper.valueToTree(productDto);
    }




    @JrpcMethod(method = HandlerName.Product.findAllById)
    public JsonNode findAllById(JsonNode params) {

        List<Long> idList = getIdList(params);

        // Getting from repository product by "idList"
        List<Product> list = productService.findAllById(idList);
        List<ProductDto> listDto = productListMapper.toDto(list);
        return objectMapper.valueToTree(listDto);
    }



    @JrpcMethod(method = HandlerName.Product.findAll)
    public JsonNode findAll(JsonNode params) {

        List<Product> list = productService.findAll();
        List<ProductDto> listDto = productListMapper.toDto(list);
        return objectMapper.valueToTree(listDto);
    }
    


    @JrpcMethod(method = HandlerName.Product.save)
    public JsonNode save(JsonNode params) {

        Product product = getProduct(params);
        productService.save(product);
        return objectMapper.valueToTree(product.getId());
    }

    @JrpcMethod(method = HandlerName.Product.delete)
    public JsonNode delete(JsonNode params) {

        Product product = getProduct(params);
        productService.delete(product);
        return null;
    }


    // ==============================================================================





    // Auxiliary method
    private Product getProduct(JsonNode params) {

        // parsing request
        Product result;
        try {
            ProductDto productDto = objectMapper.treeToValue(params, ProductDto.class);

            result = productMapper.toEntity(productDto);
            // Проверяем на валидность
            productService.validate(result);
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Jackson parse error:\n" + e.getMessage(), e);
        }
        return result;
    }


}


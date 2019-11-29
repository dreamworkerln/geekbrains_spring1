package jsonrpc.server.handlers.product;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jsonrpc.protocol.dto.base.HandlerName;
import jsonrpc.protocol.dto.product.ProductDto;
import jsonrpc.server.entities.product.Product;
import jsonrpc.server.entities.product.lists.ProductListMapper;
import jsonrpc.server.entities.product.ProductMapper;
import jsonrpc.server.handlers.base.HandlerBase;
import jsonrpc.server.handlers.base.JrpcController;
import jsonrpc.server.handlers.base.JrpcHandler;
import jsonrpc.server.repository.ProductRepository;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;


@Service
@JrpcController(path = HandlerName.Product.path)
public class ProductHandler extends HandlerBase {

    private final static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ProductRepository productRepository;
    private final ProductListMapper productListMapper;
    private final ProductMapper productMapper;





    @Autowired
    public ProductHandler(
            ObjectMapper objectMapper,
            ProductRepository productRepository,
            ProductListMapper productListMapper,
            ProductMapper productMapper) {

        super(objectMapper);

        this.productRepository = productRepository;
        this.productListMapper = productListMapper;
        this.productMapper = productMapper;
    }

    // -----------------------------------------------------------------------------


    @JrpcHandler(method = HandlerName.Product.getById)
    public JsonNode getById(JsonNode params) {

        // request id
        long id = getId(params);

        // Getting from repository product by id
        Product product = productRepository.getById(id);
        ProductDto productDto = productMapper.toDto(product);
        return objectMapper.valueToTree(productDto);
    }




    @JrpcHandler(method = HandlerName.Product.getByListId)
    public JsonNode getByListId(JsonNode params) {

        List<Long> idList = getIdList(params);

        // Getting from repository product by "id"
        List<Product> list = productRepository.getByListId(idList);
        List<ProductDto> listDto = productListMapper.toDto(list);
        return objectMapper.valueToTree(listDto);
    }



    @JrpcHandler(method = HandlerName.Product.getAll)
    public JsonNode getAll(JsonNode params) {

        List<Product> list = productRepository.getAll();
        List<ProductDto> listDto = productListMapper.toDto(list);
        return objectMapper.valueToTree(listDto);
    }
    


    @JrpcHandler(method = HandlerName.Product.put)
    public JsonNode put(JsonNode params) {

        Product product = getProduct(params);
        productRepository.put(product);
        return objectMapper.valueToTree(product.getId());
    }

    @JrpcHandler(method = HandlerName.Product.delete)
    public JsonNode delete(JsonNode params) {

        throw new NotImplementedException("ProductHandler.delete");
    }


    // ==============================================================================






    private Product getProduct(JsonNode params) {

        // parsing request
        Product result;
        try {
            ProductDto productDto = objectMapper.treeToValue(params, ProductDto.class);
            ProductDto.validate(productDto);
            result = productMapper.toEntity(productDto);
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Jackson parse error", e);
        }
        return result;
    }


}


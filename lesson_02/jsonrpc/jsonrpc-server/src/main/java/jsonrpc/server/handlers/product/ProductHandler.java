package jsonrpc.server.handlers.product;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jsonrpc.protocol.dto.product.ProductDto;
import jsonrpc.protocol.dto.base.jrpc.AbstractDto;
import jsonrpc.protocol.dto.product.lists.ProductListDto;
import jsonrpc.server.entities.base.param.GetByIdParam;
import jsonrpc.server.entities.base.param.GetByIdMapper;
import jsonrpc.server.entities.base.param.GetByListIdMapper;
import jsonrpc.server.entities.base.param.GetByListIdParam;
import jsonrpc.server.entities.product.Product;
import jsonrpc.server.entities.product.lists.ProductList;
import jsonrpc.server.entities.product.lists.ProductListMapper;
import jsonrpc.server.entities.product.ProductMapper;
import jsonrpc.server.handlers.base.HandlerBase;
import jsonrpc.server.handlers.base.JrpcController;
import jsonrpc.server.handlers.base.JrpcHandler;
import jsonrpc.server.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

import static jsonrpc.server.configuration.SpringConfiguration.MAIN_ENTITIES_PATH;



@Service
@JrpcController(path = MAIN_ENTITIES_PATH + "." + "product")
public class ProductHandler extends HandlerBase {

    private final static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ProductRepository productRepository;
    private final ProductListMapper productListMapper;
    //private final GetByIdMapper getByIdMapper;
    private final ProductMapper productMapper;

    @Autowired
    public ProductHandler(
            ObjectMapper objectMapper,
            GetByIdMapper getByIdMapper,
            GetByListIdMapper getByListIdMapper,
            ProductRepository productRepository,
            ProductListMapper productListMapper,
            ProductMapper productMapper) {

        super(objectMapper, getByIdMapper, getByListIdMapper);
        
        this.productRepository = productRepository;
        this.productListMapper = productListMapper;
        this.productMapper = productMapper;
    }


    @JrpcHandler(method = "getById")
    public AbstractDto getById(JsonNode params) {

        ProductDto result;
        GetByIdParam request = getByIdRequest(params);

        // Getting from repository product by "id"

        Product product = productRepository.getById(request.getId());

        try {
            result = productMapper.toDto(product);

        } catch (Exception e) {
            throw new IllegalArgumentException("MapStruct error", e);
        }

        return result;
    }


    

    @JrpcHandler(method = "getByListId")
    public AbstractDto getByListId(JsonNode params) {

        ProductListDto result;
        GetByListIdParam request = getByListIdRequest(params);

        // Getting from repository product by "id"

        List<Product> list = productRepository.getByListId(request.getIdList());

        try {
            // Оборачиваем List<Product> в ProductList, т.к. MapStrict не будет мапить List<Object> в Object
            // (либо я не знаю как его настроить)
            result = productListMapper.toDto(new ProductList(list));

        } catch (Exception e) {
            throw new IllegalArgumentException("MapStruct error", e);
        }

        return result;
    }

    @JrpcHandler(method = "getAll")
    public AbstractDto getAll(JsonNode params) {
        //throw new NotImplementedException("getByListId not implemented");

        ProductListDto result;
        List<Product> list = productRepository.getAll();

        try {
            result = productListMapper.toDto(new ProductList(list));

        } catch (Exception e) {
            throw new IllegalArgumentException("MapStruct error", e);
        }

        return result;



    }
}


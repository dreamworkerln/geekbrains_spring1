package jsonrpc.server.handlers.product;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jsonrpc.protocol.dto.product.ProductDto;
import jsonrpc.protocol.dto.base.jrpc.AbstractDto;
import jsonrpc.server.entities.base.param.GetById;
import jsonrpc.server.entities.base.param.GetByIdMapper;
import jsonrpc.server.entities.product.Product;
import jsonrpc.server.entities.product.ProductMapper;
import jsonrpc.server.handlers.base.HandlerBase;
import jsonrpc.server.handlers.base.JrpcController;
import jsonrpc.server.handlers.base.JrpcHandler;
import jsonrpc.server.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

import static jsonrpc.server.configuration.SpringConfiguration.MAIN_ENTITIES_PATH;

@Component
@JrpcController(path = MAIN_ENTITIES_PATH + "." + "product")
public class ProductHandler extends HandlerBase {

    private final static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ProductRepository productRepository;
    private final GetByIdMapper getByIdMapper;
    private final ProductMapper productMapper;

    @Autowired
    public ProductHandler(
            ObjectMapper objectMapper,
            GetByIdMapper getByIdMapper,
            ProductRepository productRepository,
            ProductMapper productMapper) {

        super(objectMapper, getByIdMapper);
        this.productRepository = productRepository;
        this.getByIdMapper = getByIdMapper;
        this.productMapper = productMapper;
    }


    @JrpcHandler(method = "getById")
    public AbstractDto getById(JsonNode params) {

        ProductDto result;
        GetById request = getByIdRequest(params);

        // Getting from repository product by "id"

        Product product = productRepository.getById(request.getId());

        try {
            result = productMapper.toDto(product);

        } catch (Exception e) {
            throw new IllegalArgumentException("MapStruct error", e);
        }

        return result;
    }
}


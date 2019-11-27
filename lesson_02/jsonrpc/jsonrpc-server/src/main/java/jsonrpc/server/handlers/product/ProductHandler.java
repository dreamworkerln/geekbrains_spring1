package jsonrpc.server.handlers.product;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jsonrpc.protocol.dto.base.HandlerName;
import jsonrpc.protocol.dto.base.param.IdDto;
import jsonrpc.protocol.dto.product.ProductDto;
import jsonrpc.protocol.dto.base.jrpc.AbstractDto;
import jsonrpc.protocol.dto.product.lists.ProductListDto;
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
    private final ProductListDto productListDto;
    private final ProductListMapper productListMapper;
    private final ProductMapper productMapper;
    private final IdDto idDto;





    @Autowired
    public ProductHandler(
            ObjectMapper objectMapper,
            ProductRepository productRepository,
            ProductListDto productListDto,
            ProductListMapper productListMapper,
            ProductMapper productMapper, IdDto idDto) {

        super(objectMapper);

        this.productRepository = productRepository;
        this.productListDto = productListDto;
        this.productListMapper = productListMapper;
        this.productMapper = productMapper;
        this.idDto = idDto;
    }

    // -----------------------------------------------------------------------------


    @JrpcHandler(method = HandlerName.Product.getById)
    public AbstractDto getById(JsonNode params) {

        // request id
        long id = getId(params);

        // Getting from repository product by id
        Product product = productRepository.getById(id);

        return productMapper.toDto(product);
    }




    @JrpcHandler(method = HandlerName.Product.getByListId)
    public AbstractDto getByListId(JsonNode params) {

        List<Long> idList = getIdList(params);

        // Getting from repository product by "id"
        List<Product> list = productRepository.getByListId(idList);

        // Оборачиваем List<ProductDto> в ProductListDto
        return new ProductListDto(productListMapper.toDto(list));
    }



    @JrpcHandler(method = HandlerName.Product.getAll)
    public AbstractDto getAll(JsonNode params) {

        ProductListDto result;
        List<Product> list = productRepository.getAll();

        return new ProductListDto(productListMapper.toDto(list));
    }
    


    @JrpcHandler(method = HandlerName.Product.put)
    public AbstractDto put(JsonNode params) {

        Product product = getProduct(params);
        productRepository.put(product);
        return new IdDto(product.getId());
    }

    @JrpcHandler(method = HandlerName.Product.delete)
    public AbstractDto delete(JsonNode params) {

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


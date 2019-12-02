package jsonrpc.server.controller.jrpc.storage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jsonrpc.protocol.dto.base.HandlerName;
import jsonrpc.protocol.dto.product.ProductItemDto;
import jsonrpc.server.controller.jrpc.base.AbstractJrpcController;
import jsonrpc.server.controller.jrpc.base.JrpcController;
import jsonrpc.server.controller.jrpc.base.JrpcMethod;
import jsonrpc.server.entities.product.ProductItem;
import jsonrpc.server.entities.product.mappers.ProductItemMapper;
import jsonrpc.server.entities.product.mappers.ProductItemListMapper;
import jsonrpc.server.entities.storage.StorageItem;
import jsonrpc.server.repository.StorageRepository;
import jsonrpc.server.service.StorageService;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.validation.Validator;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@Service
@JrpcController(path = HandlerName.Storage.path)
public class StorageController extends AbstractJrpcController {

    private final static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final StorageService storageService;
    private final ProductItemMapper productItemMapper;
    private final ProductItemListMapper productItemListMapper;


    public StorageController(
            ObjectMapper objectMapper,
            StorageService storageService, ProductItemMapper productItemMapper,
            ProductItemListMapper productItemListMapper) {

        super(objectMapper);

        this.storageService = storageService;
        this.productItemMapper = productItemMapper;
        this.productItemListMapper = productItemListMapper;
    }


    // -----------------------------------------------------------------------------




    @JrpcMethod(method = HandlerName.Storage.findById)
    public JsonNode findById(JsonNode params) {


        // request id
        long id = getId(params);
        ProductItem productItem = storageService.findById(id).orElse(null);
        ProductItemDto productItemDto = productItemMapper.toDto(productItem);
        return objectMapper.valueToTree(productItemDto);
    }

    @JrpcMethod(method = HandlerName.Storage.findAllById)
    public JsonNode findAllById(JsonNode params) {

        List<Long> idList = getIdList(params);
        List<ProductItem> list = storageService.findAllById(idList);
        List<ProductItemDto> listDto = productItemListMapper.toDto(list);
        return objectMapper.valueToTree(listDto);
    }




    @JrpcMethod(method = HandlerName.Storage.findAll)
    public JsonNode findAll(JsonNode params) {

        List<ProductItem> list = storageService.findAll();
        List<ProductItemDto> listDto = productItemListMapper.toDto(list);
        return objectMapper.valueToTree(listDto);
    }



    @JrpcMethod(method = HandlerName.Storage.put)
    public JsonNode put(JsonNode params) {

        ProductItem productItem = getProductItem(params);
        storageService.put(productItem.getProduct(), productItem.getCount());
        return null;
    }


    @JrpcMethod(method = HandlerName.Storage.remove)
    public JsonNode remove(JsonNode params) {

        ProductItem productItem = getProductItem(params);
        storageService.remove(productItem.getProduct(), productItem.getCount());
        return null;
    }


    @JrpcMethod(method = HandlerName.Storage.delete)
    public JsonNode delete(JsonNode params) {

        Long id = getId(params);
        storageService.delete(id);
        return null;
    }




    // ==============================================================================



    // Auxiliary method
    private ProductItem getProductItem(JsonNode params) {


        // parsing request
        ProductItem result;
        try {
            ProductItemDto productItemDto = objectMapper.treeToValue(params, ProductItemDto.class);


            // Проверка входящего DTO
            // - опустили, пуская проверяет storageService на сконверченной Entity
            result = productItemMapper.toEntity(productItemDto);

            // Проверяем на валидность
            storageService.validate(result);

        }
        catch (Exception e) {
            throw new IllegalArgumentException("Jackson parse error:\n" + e.getMessage(), e);
        }
        return result;
    }
}

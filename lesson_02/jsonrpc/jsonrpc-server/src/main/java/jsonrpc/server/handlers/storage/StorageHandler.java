package jsonrpc.server.handlers.storage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jsonrpc.protocol.dto.base.HandlerName;
import jsonrpc.protocol.dto.product.ProductDto;
import jsonrpc.protocol.dto.product.ProductItemDto;
import jsonrpc.server.entities.product.ProductItem;
import jsonrpc.server.entities.product.ProductItemMapper;
import jsonrpc.server.entities.product.lists.ProductItemListMapper;
import jsonrpc.server.handlers.base.HandlerBase;
import jsonrpc.server.handlers.base.JrpcController;
import jsonrpc.server.handlers.base.JrpcHandler;
import jsonrpc.server.repository.StorageRepository;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
@JrpcController(path = HandlerName.Storage.path)
public class StorageHandler extends HandlerBase {

    private final static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final StorageRepository storageRepository;
    private final ProductItemMapper productItemMapper;
    private final ProductItemListMapper productItemListMapper;


    public StorageHandler(
            ObjectMapper objectMapper,
            StorageRepository storageRepository,
            ProductItemMapper productItemMapper, ProductItemListMapper productItemListMapper) {

        super(objectMapper);

        this.storageRepository = storageRepository;
        this.productItemMapper = productItemMapper;
        this.productItemListMapper = productItemListMapper;
    }


    // -----------------------------------------------------------------------------




    @JrpcHandler(method = HandlerName.Storage.getById)
    public JsonNode getById(JsonNode params) {

        // request id
        long id = getId(params);
        ProductItem productItem = storageRepository.getById(id);
        ProductItemDto productItemDto = productItemMapper.toDto(productItem);
        return objectMapper.valueToTree(productItemDto);
    }

    @JrpcHandler(method = HandlerName.Storage.getByListId)
    public JsonNode getByListId(JsonNode params) {

        List<Long> idList = getIdList(params);
        List<ProductItem> list = storageRepository.getByListId(idList);
        List<ProductItemDto> listDto = productItemListMapper.toDto(list);
        return objectMapper.valueToTree(listDto);
    }




    @JrpcHandler(method = HandlerName.Storage.getAll)
    public JsonNode getAll(JsonNode params) {

        List<ProductItem> list = storageRepository.getAll();
        List<ProductItemDto> listDto = productItemListMapper.toDto(list);
        return objectMapper.valueToTree(listDto);
    }



    @JrpcHandler(method = HandlerName.Storage.put)
    public JsonNode put(JsonNode params) {

        ProductItem productItem = getProductItem(params);
        Long id = storageRepository.put(productItem.getProduct(), productItem.getCount());
        // У persisted ProductItem и Product id совпадают
        return objectMapper.valueToTree(id);
    }


    @JrpcHandler(method = HandlerName.Storage.remove)
    public JsonNode remove(JsonNode params) {

        ProductItem productItem = getProductItem(params);
        Long id = storageRepository.remove(productItem.getProduct(), productItem.getCount());
        return objectMapper.valueToTree(id);
    }


    @JrpcHandler(method = HandlerName.Storage.delete)
    public JsonNode delete(JsonNode params) {

        throw new NotImplementedException("StorageHandler.delete");
    }




    // ==============================================================================




    private ProductItem getProductItem(JsonNode params) {

        // parsing request
        ProductItem result;
        try {
            ProductItemDto productItemDto = objectMapper.treeToValue(params, ProductItemDto.class);
            ProductItemDto.validate(productItemDto);
            result = productItemMapper.toEntity(productItemDto);
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Jackson parse error", e);
        }
        return result;
    }
}

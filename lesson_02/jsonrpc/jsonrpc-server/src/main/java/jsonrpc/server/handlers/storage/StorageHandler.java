package jsonrpc.server.handlers.storage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jsonrpc.protocol.dto.base.HandlerName;
import jsonrpc.protocol.dto.base.jrpc.AbstractDto;
import jsonrpc.protocol.dto.order.OrderItemDto;
import jsonrpc.protocol.dto.product.ProductItemDto;
import jsonrpc.protocol.dto.product.lists.ProductItemListDto;
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
    public AbstractDto getById(JsonNode params) {

        // request id
        long id = getId(params);
        ProductItem productItem = storageRepository.getById(id);
        return productItemMapper.toDto(productItem);
    }

    @JrpcHandler(method = HandlerName.Storage.getByListId)
    public AbstractDto getByListId(JsonNode params) {

        List<Long> idList = getIdList(params);
        List<ProductItem> list = storageRepository.getByListId(idList);
        return new ProductItemListDto(productItemListMapper.toDto(list));
    }




    @JrpcHandler(method = HandlerName.Storage.getAll)
    public AbstractDto getAll(JsonNode params) {

        List<ProductItem> list = storageRepository.getAll();
        return new ProductItemListDto(productItemListMapper.toDto(list));
    }



    @JrpcHandler(method = HandlerName.Storage.put)
    public AbstractDto put(JsonNode params) {

        ProductItem productItem = getProductItem(params);
        storageRepository.put(productItem.getProduct(), productItem.getCount());
        return null;
    }


    @JrpcHandler(method = HandlerName.Storage.remove)
    public AbstractDto remove(JsonNode params) {

        ProductItem productItem = getProductItem(params);
        storageRepository.remove(productItem.getProduct(), productItem.getCount());
        return null;
    }


    @JrpcHandler(method = HandlerName.Storage.delete)
    public AbstractDto delete(JsonNode params) {

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

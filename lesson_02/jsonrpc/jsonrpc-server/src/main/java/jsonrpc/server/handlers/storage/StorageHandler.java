package jsonrpc.server.handlers.storage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jsonrpc.protocol.dto.base.jrpc.AbstractDto;
import jsonrpc.protocol.dto.product.lists.ProductItemListDto;
import jsonrpc.protocol.dto.product.lists.ProductListDto;
import jsonrpc.server.entities.base.param.GetByIdParam;
import jsonrpc.server.entities.base.param.GetByIdMapper;
import jsonrpc.server.entities.base.param.GetByListIdMapper;
import jsonrpc.server.entities.base.param.GetByListIdParam;
import jsonrpc.server.entities.product.ProductItem;
import jsonrpc.server.entities.product.ProductItemMapper;
import jsonrpc.server.entities.product.lists.ProductItemList;
import jsonrpc.server.entities.product.lists.ProductItemListMapper;
import jsonrpc.server.entities.product.lists.ProductList;
import jsonrpc.server.entities.storage.Storage;
import jsonrpc.server.handlers.base.HandlerBase;
import jsonrpc.server.handlers.base.JrpcController;
import jsonrpc.server.handlers.base.JrpcHandler;
import jsonrpc.server.repository.StorageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

import static jsonrpc.server.configuration.SpringConfiguration.MAIN_ENTITIES_PATH;

@Service
@JrpcController(path = MAIN_ENTITIES_PATH + "." + "storage")
public class StorageHandler extends HandlerBase {

    private final static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final StorageRepository storageRepository;
    private final ProductItemMapper productItemMapper;
    private final ProductItemListMapper productItemListMapper;


    public StorageHandler(
            ObjectMapper objectMapper,
            StorageRepository storageRepository,
            GetByIdMapper getByIdMapper,
            GetByListIdMapper getByListIdMapper,
            ProductItemMapper productItemMapper, ProductItemListMapper productItemListMapper) {

        super(objectMapper, getByIdMapper, getByListIdMapper);

        this.storageRepository = storageRepository;
        this.productItemMapper = productItemMapper;
        this.productItemListMapper = productItemListMapper;
    }

    @JrpcHandler(method = "getById")
    public AbstractDto getById(JsonNode params) {

        AbstractDto result;
        GetByIdParam request = getByIdRequest(params);

        // Getting from repository ProductItem by product.id

        ProductItem pi = storageRepository.getById(request.getId());

        try {
            result = productItemMapper.toDto(pi);

        } catch (Exception e) {
            throw new IllegalArgumentException("MapStruct error", e);
        }

        return result;
    }

    @JrpcHandler(method = "getByListId")
    public AbstractDto getByListId(JsonNode params) {

        ProductItemListDto result;
        GetByListIdParam request = getByListIdRequest(params);

        // Getting from repository product by "id"

        List<ProductItem> list = storageRepository.getByListId(request.getIdList());

        try {
            // Оборачиваем List<Product> в ProductList, т.к. MapStrict не будет мапить List<Object> в Object
            // (либо я не знаю как его настроить)
            result = productItemListMapper.toDto(new ProductItemList(list));

        } catch (Exception e) {
            throw new IllegalArgumentException("MapStruct error", e);
        }

        return result;
    }




    @JrpcHandler(method = "getAll")
    public AbstractDto getAll(JsonNode params) {

        AbstractDto result = null;

        List<ProductItem> list = storageRepository.getAll();

        try {
            result = productItemListMapper.toDto(new ProductItemList(list));

        } catch (Exception e) {
            throw new IllegalArgumentException("MapStruct error", e);
        }

        return result;
    }
}

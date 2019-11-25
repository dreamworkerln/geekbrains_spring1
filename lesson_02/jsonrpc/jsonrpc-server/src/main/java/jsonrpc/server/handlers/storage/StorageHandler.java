package jsonrpc.server.handlers.storage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jsonrpc.protocol.dto.base.jrpc.AbstractDto;
import jsonrpc.protocol.dto.base.jrpc.AbstractDtoPersisted;
import jsonrpc.protocol.dto.product.ProductItemDto;
import jsonrpc.protocol.dto.storage.StorageDto;
import jsonrpc.server.entities.base.param.GetById;
import jsonrpc.server.entities.base.param.GetByIdMapper;
import jsonrpc.server.entities.product.ProductItem;
import jsonrpc.server.entities.product.ProductItemMapper;
import jsonrpc.server.entities.storage.StorageMapper;
import jsonrpc.server.handlers.base.HandlerBase;
import jsonrpc.server.handlers.base.JrpcController;
import jsonrpc.server.handlers.base.JrpcHandler;
import jsonrpc.server.repository.StorageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

import static jsonrpc.server.configuration.SpringConfiguration.MAIN_ENTITIES_PATH;

@Component
@JrpcController(path = MAIN_ENTITIES_PATH + "." + "storage")
public class StorageHandler extends HandlerBase {

    private final static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final StorageRepository storageRepository;
    private final GetByIdMapper getByIdMapper;
    private final StorageMapper storageMapper;
    private final ProductItemMapper productItemMapper;


    public StorageHandler(
            ObjectMapper objectMapper,
            StorageRepository storageRepository,
            GetByIdMapper getByIdMapper,
            StorageMapper storageMapper, ProductItemMapper productItemMapper) {

        super(objectMapper, getByIdMapper);

        this.storageRepository = storageRepository;
        this.getByIdMapper = getByIdMapper;
        this.storageMapper = storageMapper;
        this.productItemMapper = productItemMapper;
    }

    @JrpcHandler(method = "getById")
    public AbstractDto getById(JsonNode params) {

        AbstractDtoPersisted result;
        GetById request = getByIdRequest(params);

        // Getting from ыещкфпу repository ProductItem by product.id

        ProductItem pi = storageRepository.getById(request.getId());

        try {
            result = productItemMapper.toDto(pi);

        } catch (Exception e) {
            throw new IllegalArgumentException("MapStruct error", e);
        }

        return result;
    }
}

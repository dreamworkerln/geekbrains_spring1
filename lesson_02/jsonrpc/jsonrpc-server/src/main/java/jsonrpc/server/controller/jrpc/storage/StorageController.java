package jsonrpc.server.controller.jrpc.storage;

import com.fasterxml.jackson.databind.JsonNode;
import jsonrpc.protocol.dto.base.HandlerName;
import jsonrpc.protocol.dto.product.ProductItemDto;
import jsonrpc.server.controller.jrpc.base.AbstractConverter;
import jsonrpc.server.controller.jrpc.base.JrpcController;
import jsonrpc.server.controller.jrpc.base.JrpcMethod;
import jsonrpc.server.entities.product.ProductItem;
import jsonrpc.server.entities.product.mappers.ProductMapper;
import jsonrpc.server.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Set;

@Service
@JrpcController(path = HandlerName.Storage.path)
public class StorageController {

    private final static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final StorageService storageService;
    private final StorageConverter converter;


    public StorageController(
            StorageService storageService,
            StorageConverter converter) {

        this.storageService = storageService;
        this.converter = converter;
    }


    // -----------------------------------------------------------------------------


    @JrpcMethod(method = HandlerName.Storage.findByProductId)
    public JsonNode findByProductId(JsonNode params) {


        // request id
        long id = converter.getId(params);
        ProductItem productItem = storageService.findByProductId(id).orElse(null);
        return converter.toJsonProductItemDto(productItem);
    }

    @JrpcMethod(method = HandlerName.Storage.findAllByProductId)
    public JsonNode findAllByProductId(JsonNode params) {

        List<Long> idList = converter.getIdList(params);
        List<ProductItem> list = storageService.findAllByProductId(idList);
        return converter.toJsonProductItemListDto(list);
    }


    @JrpcMethod(method = HandlerName.Storage.findAll)
    public JsonNode findAll(JsonNode params) {

        List<ProductItem> list = storageService.findAll();
        return converter.toJsonProductItemListDto(list);
    }


    @JrpcMethod(method = HandlerName.Storage.put)
    public JsonNode put(JsonNode params) {

        ProductItem productItem = converter.toProductItem(params);
        storageService.put(productItem.getProduct(), productItem.getCount());
        return null;
    }


    @JrpcMethod(method = HandlerName.Storage.remove)
    public JsonNode remove(JsonNode params) {

        ProductItem productItem = converter.toProductItem(params);
        storageService.remove(productItem.getProduct(), productItem.getCount());
        return null;
    }


    @JrpcMethod(method = HandlerName.Storage.delete)
    public JsonNode delete(JsonNode params) {

        Long id = converter.getId(params);
        storageService.delete(id);
        return null;
    }


    // ==============================================================================


    @Service
    static class StorageConverter extends AbstractConverter {

        private final ProductMapper productMapper;

        public StorageConverter(ProductMapper productMapper) {

            this.productMapper = productMapper;
        }

        public ProductItem toProductItem(JsonNode params) {
            try {
                ProductItemDto dto = objectMapper.treeToValue(params, ProductItemDto.class);
                ProductItem result = productMapper.toItemEntity(dto);
                validate(result);
                return result;
            }
            // It's request, only IllegalArgumentException - will lead to HTTP 400 ERROR
            catch (Exception e) {
                throw new IllegalArgumentException("Jackson parse error:\n" + e.getMessage(), e);
            }
        }

        public JsonNode toJsonProductItemDto(ProductItem productItem) {
            ProductItemDto dto = productMapper.toItemDto(productItem);
            return objectMapper.valueToTree(dto);
        }


        public JsonNode toJsonProductItemListDto(List<ProductItem> productItemList) {
            List<ProductItemDto> listDto = productMapper.toItemDtoList(productItemList);
            return objectMapper.valueToTree(listDto);
        }

        public void validate(ProductItem productItem) {

            Set<ConstraintViolation<ProductItem>> violations = validator.validate(productItem);
            if (violations.size() != 0) {
                throw new ConstraintViolationException("ProductItem validation failed", violations);
            }
        }
    }
}



//    // Auxiliary method
//    private ProductItem getProductItem(JsonNode params) {
//
//
//        // parsing request
//        ProductItem result;
//        try {
//            ProductItemDto productItemDto = objectMapper.jsonToDto(params, ProductItemDto.class);
//
//
//            // Проверка входящего DTO
//            // - опустили, пуская проверяет storageService на сконверченной Entity
//            result = productItemMapper.toItemEntity(productItemDto);
//
//            // Проверяем на валидность
//            storageService.validate(result);
//
//        }
//        catch (Exception e) {
//            throw new IllegalArgumentException("Jackson parse error:\n" + e.getMessage(), e);
//        }
//        return result;
//    }


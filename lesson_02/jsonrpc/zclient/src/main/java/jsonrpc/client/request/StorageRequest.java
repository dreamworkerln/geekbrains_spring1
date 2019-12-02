package jsonrpc.client.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jsonrpc.client.configuration.ClientProperties;
import jsonrpc.client.request.base.AbstractRequest;
import jsonrpc.protocol.dto.base.HandlerName;
import jsonrpc.protocol.dto.product.ProductItemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Arrays;
import java.util.List;
import java.util.Set;


@Service
public class StorageRequest extends AbstractRequest {


    @Autowired
    public StorageRequest(ApplicationContext context,
                          ObjectMapper objectMapper,
                          ClientProperties clientProperties) {

        super(context, objectMapper, clientProperties);
    }


    public ProductItemDto getById(long id) throws JsonProcessingException {

        String uri = HandlerName.Storage.path + "." + HandlerName.Storage.findByProductId;

        JsonNode response = performRequest(1000L, uri, id);
        return objectMapper.treeToValue(response, ProductItemDto.class);

    }


    public List<ProductItemDto> getByIdList(List<Long> list) throws JsonProcessingException {

        String uri = HandlerName.Storage.path + "." + HandlerName.Storage.findAllByProductId;

        // ASAP EDC !!! CHECK
        //Arrays.asList(1L, 2L, 3L, 999L)
        //new ArrayList<>(Arrays.asList(1L, 2L, 3L, 999L))
        JsonNode response = performRequest(1000L, uri, list);
        return Arrays.asList(objectMapper.treeToValue(response, ProductItemDto[].class));
    }


    public List<ProductItemDto> getAll() throws JsonProcessingException {

        String uri = HandlerName.Storage.path + "." + HandlerName.Storage.findAll;
        JsonNode response = performRequest(1000L, uri, null);
        return Arrays.asList(objectMapper.treeToValue(response, ProductItemDto[].class));
    }


    public Long put(Long productId, int count) throws JsonProcessingException {

        String uri = HandlerName.Storage.path + "." + HandlerName.Storage.put;
        ProductItemDto productItemDto = context.getBean("productItemDto", ProductItemDto.class);

        productItemDto.setProductId(productId);
        productItemDto.setCount(count);

        //ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        //Validator validator = factory.getValidator();
        //Set<ConstraintViolation<ProductItemDto>> violations = validator.validate(productItemDto);

        JsonNode response = performRequest(1000L, uri, productItemDto);
        return objectMapper.treeToValue(response, Long.class);
    }


    public Long remove(Long productId, int count) throws JsonProcessingException {

        String uri = HandlerName.Storage.path + "." + HandlerName.Storage.remove;
        ProductItemDto productItemDto = context.getBean("productItemDto", ProductItemDto.class);
        productItemDto.setProductId(productId);
        productItemDto.setCount(count);

        JsonNode response = performRequest(1000L, uri, productItemDto);
        return objectMapper.treeToValue(response, Long.class);
    }




}

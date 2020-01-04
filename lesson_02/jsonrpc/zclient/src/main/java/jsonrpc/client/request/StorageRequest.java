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
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;


@Service
public class StorageRequest extends AbstractRequest {


    public ProductItemDto getById(long id) throws JsonProcessingException {

        String uri = HandlerName.StorageN.path + "." + HandlerName.StorageN.findByProductId;

        JsonNode response = performRequest(1000L, uri, id);
        return objectMapper.treeToValue(response, ProductItemDto.class);

    }


    public List<ProductItemDto> getByIdList(List<Long> list) throws JsonProcessingException {

        String uri = HandlerName.StorageN.path + "." + HandlerName.StorageN.findAllByProductId;
        JsonNode response = performRequest(1000L, uri, list);
        return Arrays.asList(objectMapper.treeToValue(response, ProductItemDto[].class));
    }


    public List<ProductItemDto> getAll() throws JsonProcessingException {

        String uri = HandlerName.StorageN.path + "." + HandlerName.StorageN.findAll;
        JsonNode response = performRequest(1000L, uri, null);
        return Arrays.asList(objectMapper.treeToValue(response, ProductItemDto[].class));
    }


    public Long put(Long productId, int count) throws JsonProcessingException {

        String uri = HandlerName.StorageN.path + "." + HandlerName.StorageN.put;
        ProductItemDto productItemDto =  new ProductItemDto();//context.getBean("productItemDto", ProductItemDto.class);

        productItemDto.setProductId(productId);
        productItemDto.setCount(count);

        //ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        //Validator validator = factory.getValidator();
        //Set<ConstraintViolation<ProductItemDto>> violations = validator.validate(productItemDto);

        JsonNode response = performRequest(1000L, uri, productItemDto);
        return objectMapper.treeToValue(response, Long.class);
    }


    public Long remove(Long productId, int count) throws JsonProcessingException {

        String uri = HandlerName.StorageN.path + "." + HandlerName.StorageN.remove;
        ProductItemDto productItemDto = new ProductItemDto(); //context.getBean("productItemDto", ProductItemDto.class);
        productItemDto.setProductId(productId);
        productItemDto.setCount(count);

        JsonNode response = performRequest(1000L, uri, productItemDto);
        return objectMapper.treeToValue(response, Long.class);
    }




}

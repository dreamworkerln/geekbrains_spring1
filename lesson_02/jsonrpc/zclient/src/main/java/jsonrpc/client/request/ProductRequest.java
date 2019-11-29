package jsonrpc.client.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jsonrpc.client.configuration.ClientProperties;
import jsonrpc.protocol.dto.base.HandlerName;
import jsonrpc.protocol.dto.product.ProductDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


// надеюсь, методы, вызванные одновременно из разных потоков
// будут работать без глюков
@Service
public class ProductRequest extends RequestBase {

    private static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    public ProductRequest(ApplicationContext context,
                          ObjectMapper objectMapper,
                          ClientProperties clientProperties) {

        super(context, objectMapper, clientProperties);
    }


    public ProductDto getById(long id) throws JsonProcessingException {

        String uri = HandlerName.Product.path + "." + HandlerName.Product.getById;
        JsonNode response = performRequest(1000L, uri, id);
        return objectMapper.treeToValue(response, ProductDto.class);
    }


    public List<ProductDto> getByListId(List<Long> list) throws JsonProcessingException {

        String uri = HandlerName.Product.path + "." + HandlerName.Product.getByListId;

        // ASAP EDC !!!! CHECK
        // Arrays.asList(1L, 2L, 3L, 999L)
        // new ArrayList<>(Arrays.asList(1L, 2L, 3L, 999L))

        JsonNode response = performRequest(1000L, uri, list);
        // https://stackoverflow.com/questions/6349421/how-to-use-jackson-to-deserialise-an-array-of-objects
        return Arrays.asList(objectMapper.treeToValue(response, ProductDto[].class));
    }


    public List<ProductDto> getAll() throws JsonProcessingException {

        String uri = HandlerName.Product.path + "." + HandlerName.Product.getAll;
        JsonNode response = performRequest(1000L, uri, null);
        return Arrays.asList(objectMapper.treeToValue(response, ProductDto[].class));
    }
}

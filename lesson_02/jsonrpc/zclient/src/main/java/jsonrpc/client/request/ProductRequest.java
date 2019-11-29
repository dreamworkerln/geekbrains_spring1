package jsonrpc.client.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jsonrpc.client.configuration.ClientProperties;
import jsonrpc.protocol.dto.base.HandlerName;
import jsonrpc.protocol.dto.base.jrpc.JrpcRequest;
import jsonrpc.protocol.dto.base.param.IdDto;
import jsonrpc.protocol.dto.base.param.ListIdDto;
import jsonrpc.protocol.dto.product.ProductDto;
import jsonrpc.utils.Rest;
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
        IdDto idDto = context.getBean(IdDto.class);
        idDto.setId(id);
        ResponseEntity<String> response = performRequest(1000L, uri, idDto);
        log.warn(response.getStatusCode().toString() + "\n" + response.getBody());

        JsonNode result = objectMapper.readTree(response.getBody()).get("result");
        return objectMapper.treeToValue(result, ProductDto.class);
    }


    public List<ProductDto> getByListId(long id) throws JsonProcessingException {

        String uri = HandlerName.Product.path + "." + HandlerName.Product.getByListId;

        ListIdDto listIdDto = context.getBean(ListIdDto.class);
        listIdDto.setList(new ArrayList<>(Arrays.asList(1L, 2L, 3L, 999L)));

        ResponseEntity<String> response = performRequest(1000L, uri, listIdDto);
        log.warn(response.getStatusCode().toString() + "\n" + response.getBody());

        // https://stackoverflow.com/questions/6349421/how-to-use-jackson-to-deserialise-an-array-of-objects
        JsonNode result = objectMapper.readTree(response.getBody()).get("result").get("productList");
        return Arrays.asList(objectMapper.treeToValue(result, ProductDto[].class));
    }


    public List<ProductDto> getAll() throws JsonProcessingException {

        String uri = HandlerName.Product.path + "." + HandlerName.Product.getAll;
        ResponseEntity<String> response = performRequest(1000L, uri, null);
        log.warn(response.getStatusCode().toString() + "\n" + response.getBody());

        JsonNode result = objectMapper.readTree(response.getBody()).get("result").get("productList");
        return Arrays.asList(objectMapper.treeToValue(result, ProductDto[].class));
    }
}

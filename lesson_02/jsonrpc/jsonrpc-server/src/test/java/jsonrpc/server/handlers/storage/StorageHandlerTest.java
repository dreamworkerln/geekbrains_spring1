package jsonrpc.server.handlers.storage;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jsonrpc.protocol.dto.base.jrpc.JrpcRequest;
import jsonrpc.protocol.dto.product.ProductDto;
import jsonrpc.protocol.dto.storage.StorageDto;
import jsonrpc.server.TestSuite;
import jsonrpc.server.configuration.ConfigProperties;
import jsonrpc.server.configuration.SpringConfiguration;
import jsonrpc.server.entities.base.param.GetById;
import jsonrpc.server.entities.base.param.GetByIdMapper;
import jsonrpc.server.entities.base.param.GetByIdMapperImpl;
import jsonrpc.server.entities.product.Product;
import jsonrpc.server.utils.Rest;
import jsonrpc.server.utils.RestFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;

import java.lang.invoke.MethodHandles;

@EnableConfigurationProperties
@SpringBootTest(classes = {
        SpringConfiguration.class,
        GetById.class,
        ProductDto.class,
        StorageDto.class,
        JrpcRequest.class,
        GetByIdMapperImpl.class,
        ConfigProperties.class})


public class StorageHandlerTest {

    private static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static Rest rest; // Wrapper of RestTemplate

    @Autowired
    private ApplicationContext context;

    @BeforeAll
    static void setup() {
        TestSuite.INSTANCE.init();
        rest = RestFactory.getRest(true, true);
    }


    @Test
    void getById() throws JsonProcessingException {


        ConfigProperties configProperties = context.getBean(ConfigProperties.class);


        System.out.println(configProperties.getHostName());


        Long id = 1L;

        ObjectMapper objectMapper = context.getBean(ObjectMapper.class);


        JrpcRequest jrpcRequest = context.getBean(JrpcRequest.class);
        GetById getById = context.getBean(GetById.class);

        GetByIdMapper getByIdMapper = context.getBean(GetByIdMapper.class);
        getById.setId(id);
        jrpcRequest.setId(22L);
        String methodName = GetById.class.getSimpleName();
        methodName = Character.toLowerCase(methodName.charAt(0)) + methodName.substring(1);
        jrpcRequest.setMethod(SpringConfiguration.Controller.Handlers.Shop.STORAGE + "." + methodName);
        jrpcRequest.setParams(getByIdMapper.toDto(getById));

        String json = objectMapper.writeValueAsString(jrpcRequest);
        log.info(json);

        rest.setCustomHeader("token", "f229fbea-a4b9-40a8-b8ee-e2b47bc1391d");
        ResponseEntity<String> response = rest.post("http://localhost:8084/api", json);
        log.info(response.getStatusCode().toString() + "\n" + response.getBody());

    }
}


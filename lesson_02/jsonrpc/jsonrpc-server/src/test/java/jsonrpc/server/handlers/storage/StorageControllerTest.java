package jsonrpc.server.handlers.storage;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jsonrpc.protocol.dto.base.HandlerName;
import jsonrpc.protocol.dto.base.jrpc.JrpcRequest;
import jsonrpc.protocol.dto.product.ProductItemDto;
import jsonrpc.server.TestSuite;
import jsonrpc.server.configuration.ConfigProperties;
import jsonrpc.server.configuration.BeanConfiguration;
import jsonrpc.utils.Rest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;

@EnableConfigurationProperties
@SpringBootTest(classes = {
        BeanConfiguration.class,
//        JrpcRequest.class,
//        ProductItemDto.class,
        ConfigProperties.class})


public class StorageControllerTest {

    private static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private ApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JrpcRequest jrpcRequest;

    @Autowired
    private Rest rest;

    @BeforeAll
    static void setup() {
        TestSuite.INSTANCE.init();
    }

    @SuppressWarnings("Duplicates")
    @BeforeEach
    void beforeEach() {

        rest = context.getBean(Rest.class);
        objectMapper = context.getBean(ObjectMapper.class);
        jrpcRequest = context.getBean(JrpcRequest.class);
    }


    @Test
    void getById() throws JsonProcessingException {

        jrpcRequest.setId(22L);

        jrpcRequest.setMethod(HandlerName.Storage.path + "." + HandlerName.Storage.findById);
        jrpcRequest.setParams(objectMapper.valueToTree(1L));

        String json = objectMapper.writeValueAsString(jrpcRequest);
        log.info("REQUEST\n" + json);

        ResponseEntity<String> response = rest.post("http://localhost:8084/api", json);
        log.info(response.getStatusCode().toString() + "\n" + response.getBody());
    }

    @Test
    void getByIdList() throws JsonProcessingException {

        jrpcRequest.setId(22L);
        jrpcRequest.setMethod(HandlerName.Storage.path + "." + HandlerName.Storage.findAllById);
        jrpcRequest.setParams(objectMapper.valueToTree(new ArrayList<>(Arrays.asList(1L, 2L, 3L, 999L))));

        String json = objectMapper.writeValueAsString(jrpcRequest);
        log.info("REQUEST:\n" + json);

        ResponseEntity<String> response = rest.post("http://localhost:8084/api", json);
        log.info(response.getStatusCode().toString() + "\n" + response.getBody());
        log.info("Падать, если к-л из элементов (999L) не найден или отдавать, что нашли без ошибки?");

    }


    @Test
    void getAll() throws JsonProcessingException {

        jrpcRequest.setId(22L);

        jrpcRequest.setMethod(HandlerName.Storage.path + "." + HandlerName.Storage.findAll);

        String json = objectMapper.writeValueAsString(jrpcRequest);
        log.info("REQUEST:\n" + json);

        ResponseEntity<String> response = rest.post("http://localhost:8084/api", json);
        log.info(response.getStatusCode().toString() + "\n" + response.getBody());

    }

    @Test
    void put() throws JsonProcessingException {

        jrpcRequest.setId(22L);

        ProductItemDto productItemDto = context.getBean("productItemDto", ProductItemDto.class);
        productItemDto.setProductId(2L);
        productItemDto.setCount(200);

        jrpcRequest.setMethod(HandlerName.Storage.path + "." + HandlerName.Storage.put);
        jrpcRequest.setParams(objectMapper.valueToTree(productItemDto));

        String json = objectMapper.writeValueAsString(jrpcRequest);
        log.info("REQUEST:\n" + json);

        ResponseEntity<String> response = rest.post("http://localhost:8084/api", json);
        log.info(response.getStatusCode().toString() + "\n" + response.getBody());
    }

}


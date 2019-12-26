package jsonrpc.resourceserver.handlers.storage;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jsonrpc.protocol.dto.base.HandlerName;
import jsonrpc.protocol.jrpc.request.JrpcRequest;
import jsonrpc.protocol.dto.product.ProductItemDto;
import jsonrpc.resourceserver.TestSuite;
import jsonrpc.resourceserver.configuration.ConfigProperties;
import jsonrpc.resourceserver.configuration.BeanConfiguration;
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


public class StorageNControllerTest {

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private ApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Rest rest;

    JrpcRequest jrpcRequest;

    @BeforeAll
    static void setup() {
        TestSuite.INSTANCE.init();
    }

    @SuppressWarnings("Duplicates")
    @BeforeEach
    void beforeEach() {

        rest = context.getBean(Rest.class);
        objectMapper = context.getBean(ObjectMapper.class);
        jrpcRequest = new JrpcRequest(); //context.getBean(JrpcRequest.class);
    }


    @Test
    void getById() throws JsonProcessingException {

        jrpcRequest.setId(22L);

        jrpcRequest.setMethod(HandlerName.StorageN.path + "." + HandlerName.StorageN.findByProductId);
        jrpcRequest.setParams(objectMapper.valueToTree(1L));

        String json = objectMapper.writeValueAsString(jrpcRequest);
        log.info("REQUEST\n" + json);

        ResponseEntity<String> response = rest.post("http://localhost:8084/api", json);
        log.info(response.getStatusCode().toString() + "\n" + response.getBody());
    }

    @Test
    void getByIdList() throws JsonProcessingException {

        jrpcRequest.setId(22L);
        jrpcRequest.setMethod(HandlerName.StorageN.path + "." + HandlerName.StorageN.findAllByProductId);
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

        jrpcRequest.setMethod(HandlerName.StorageN.path + "." + HandlerName.StorageN.findAll);

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

        jrpcRequest.setMethod(HandlerName.StorageN.path + "." + HandlerName.StorageN.put);
        jrpcRequest.setParams(objectMapper.valueToTree(productItemDto));

        String json = objectMapper.writeValueAsString(jrpcRequest);
        log.info("REQUEST:\n" + json);

        ResponseEntity<String> response = rest.post("http://localhost:8084/api", json);
        log.info(response.getStatusCode().toString() + "\n" + response.getBody());
    }

}


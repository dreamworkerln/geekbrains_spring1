package jsonrpc.server.handlers.product;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jsonrpc.protocol.dto.base.HandlerName;
import jsonrpc.protocol.dto.base.jrpc.JrpcRequest;
import jsonrpc.protocol.dto.base.param.IdDto;
import jsonrpc.protocol.dto.base.param.ListIdDto;
import jsonrpc.protocol.dto.product.ProductDto;
import jsonrpc.server.TestSuite;
import jsonrpc.server.configuration.ConfigProperties;
import jsonrpc.server.configuration.SpringConfiguration;
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
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;

@EnableConfigurationProperties
@SpringBootTest(classes = {
        SpringConfiguration.class,
        JrpcRequest.class,
        IdDto.class,
        ListIdDto.class,
        ConfigProperties.class})


public class ProductHandlerTest {

    private static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private ApplicationContext context;


    private ObjectMapper objectMapper;
    private JrpcRequest jrpcRequest;
    private IdDto idDto;
    private ListIdDto listIdDto;
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
        idDto = context.getBean(IdDto.class);
        listIdDto = context.getBean(ListIdDto.class);
    }

    @Test
    void configTest() {

        // testing configuration
        ConfigProperties configProperties = context.getBean(ConfigProperties.class);
        System.out.println(configProperties.getHostName());
    }


    // ===================================================================================
    //                                        TESTS
    // ===================================================================================

    @Test
    void getById() throws JsonProcessingException {

        // jrpc id
        jrpcRequest.setId(22L);

        // request params
        idDto.setId(1L);

        // specify handler and method name
        jrpcRequest.setMethod(HandlerName.Product.path + "." + HandlerName.Product.getById);
        jrpcRequest.setParams(idDto);

        // producing json
        String json = objectMapper.writeValueAsString(jrpcRequest);
        log.info("REQUEST:\n" + json);

        // perform request
        ResponseEntity<String> response = rest.post("http://localhost:8084/api", json);
        log.info(response.getStatusCode().toString() + "\n" + response.getBody());

    }




    @Test
    void getByListId() throws JsonProcessingException {

        jrpcRequest.setId(22L);
        listIdDto.setList(new ArrayList<>(Arrays.asList(1L, 2L, 3L, 999L)));

        jrpcRequest.setMethod(HandlerName.Product.path + "." + HandlerName.Product.getByListId);
        jrpcRequest.setParams(listIdDto);

        String json = objectMapper.writeValueAsString(jrpcRequest);
        log.info("REQUEST:\n" + json);

        ResponseEntity<String> response = rest.post("http://localhost:8084/api", json);
        log.info(response.getStatusCode().toString() + "\n" + response.getBody());
        log.info("Падать, если к-л из элементов (999L) не найден или отдавать, что нашли без ошибки?");

    }







    @Test
    void getAll() throws JsonProcessingException {

        jrpcRequest.setId(22L);

        jrpcRequest.setMethod(HandlerName.Product.path + "." + HandlerName.Product.getAll);
        String json = objectMapper.writeValueAsString(jrpcRequest);
        log.info("REQUEST:\n" + json);

        ResponseEntity<String> response = rest.post("http://localhost:8084/api", json);
        log.info(response.getStatusCode().toString() + "\n" + response.getBody());

    }



    @Test
    void put() throws JsonProcessingException {

        jrpcRequest.setId(22L);

        ProductDto productDto = context.getBean(ProductDto.class);

        productDto.setName("Самогон домашний");
        productDto.setTestDate(Instant.EPOCH);
        productDto.setVcode("123");

        jrpcRequest.setMethod(HandlerName.Product.path + "." + HandlerName.Product.put);
        jrpcRequest.setParams(productDto);

        String json = objectMapper.writeValueAsString(jrpcRequest);
        log.info("REQUEST:\n" + json);

        ResponseEntity<String> response = rest.post("http://localhost:8084/api", json);
        log.info(response.getStatusCode().toString() + "\n" + response.getBody());
    }
}

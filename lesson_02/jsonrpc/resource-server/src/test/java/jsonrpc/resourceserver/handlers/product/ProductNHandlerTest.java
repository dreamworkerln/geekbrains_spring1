package jsonrpc.resourceserver.handlers.product;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jsonrpc.protocol.dto.base.HandlerName;
import jsonrpc.protocol.jrpc.request.JrpcRequest;
import jsonrpc.protocol.dto.product.ProductDto;
import jsonrpc.resourceserver.TestSuite;
import jsonrpc.resourceserver.configuration.ConfigProperties;
import jsonrpc.resourceserver.configuration.BeanConfiguration;
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
import java.util.Arrays;

@EnableConfigurationProperties
@SpringBootTest(classes = {
        BeanConfiguration.class,
        ConfigProperties.class
})
//        JrpcRequest.class


public class ProductNHandlerTest {

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private ApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired

    JrpcRequest jrpcRequest;

    @BeforeAll
    static void setup() {
        TestSuite.INSTANCE.init();
    }

    @SuppressWarnings("Duplicates")
    @BeforeEach
    void beforeEach() {

        //rest = context.getBean(Rest.class);
        objectMapper = context.getBean(ObjectMapper.class);
        jrpcRequest =  new JrpcRequest();//context.getBean(JrpcRequest.class);
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

        // specify handler and method name, params
        jrpcRequest.setMethod(HandlerName.ProductN.path + "." + HandlerName.ProductN.findById);
        jrpcRequest.setParams(objectMapper.valueToTree(1L));

        // producing json
        String json = objectMapper.writeValueAsString(jrpcRequest);
        log.info("REQUEST:\n" + json);

        // perform request
        //ResponseEntity<String> response = rest.post("http://localhost:8084/api", json);
        //log.info(response.getStatusCode().toString() + "\n" + response.getBody());

    }




    @Test
    void getByIdList() throws JsonProcessingException {

        jrpcRequest.setId(22L);

        jrpcRequest.setMethod(HandlerName.ProductN.path + "." + HandlerName.ProductN.findAllById);
        jrpcRequest.setParams(objectMapper.valueToTree(Arrays.asList(1L, 2L, 3L, 999L)));

        String json = objectMapper.writeValueAsString(jrpcRequest);
        log.info("REQUEST:\n" + json);

        //ResponseEntity<String> response = rest.post("http://localhost:8084/api", json);
        //log.info(response.getStatusCode().toString() + "\n" + response.getBody());
        //log.info("Падать, если к-л из элементов (999L) не найден или отдавать, что нашли без ошибки?");

    }







    @Test
    void getAll() throws JsonProcessingException {

        jrpcRequest.setId(22L);

        jrpcRequest.setMethod(HandlerName.ProductN.path + "." + HandlerName.ProductN.findAll);
        String json = objectMapper.writeValueAsString(jrpcRequest);
        log.info("REQUEST:\n" + json);

        //ResponseEntity<String> response = rest.post("http://localhost:8084/api", json);
        //log.info(response.getStatusCode().toString() + "\n" + response.getBody());

    }



    @Test
    void put() throws JsonProcessingException {

        jrpcRequest.setId(22L);

        ProductDto productDto = context.getBean(ProductDto.class);

        productDto.setName("Самогон домашний");
        productDto.setTestDate(Instant.EPOCH);
        productDto.setVcode("123");

        jrpcRequest.setMethod(HandlerName.ProductN.path + "." + HandlerName.ProductN.save);
        jrpcRequest.setParams(objectMapper.valueToTree(productDto));

        String json = objectMapper.writeValueAsString(jrpcRequest);
        log.info("REQUEST:\n" + json);

        //ResponseEntity<String> response = rest.post("http://localhost:8084/api", json);
        //log.info(response.getStatusCode().toString() + "\n" + response.getBody());
    }
}

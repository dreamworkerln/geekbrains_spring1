package jsonrpc.server.handlers.storage;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jsonrpc.protocol.dto.base.jrpc.JrpcRequest;
import jsonrpc.protocol.dto.base.param.GetAllParamDto;
import jsonrpc.protocol.dto.base.param.GetByIdParamDto;
import jsonrpc.protocol.dto.base.param.GetByListIdParamDto;
import jsonrpc.protocol.dto.order.OrderDto;
import jsonrpc.protocol.dto.order.request.PutOrderParamDto;
import jsonrpc.protocol.dto.product.ProductDto;
import jsonrpc.protocol.dto.storage.StorageDto;
import jsonrpc.server.TestSuite;
import jsonrpc.server.configuration.ConfigProperties;
import jsonrpc.server.configuration.SpringConfiguration;
import jsonrpc.server.entities.base.param.GetByIdParam;
import jsonrpc.server.entities.base.param.GetByIdMapper;
import jsonrpc.server.entities.base.param.GetByIdMapperImpl;
import jsonrpc.server.entities.order.Order;
import jsonrpc.server.utils.Rest;
import jsonrpc.server.utils.RestFactory;
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

import static jsonrpc.server.TestSuite.TOKEN;

@EnableConfigurationProperties
@SpringBootTest(classes = {
        SpringConfiguration.class,
        GetByIdParamDto.class,
        GetByListIdParamDto.class,
        GetAllParamDto.class,
        JrpcRequest.class,
        GetByIdMapperImpl.class,
        ConfigProperties.class})


public class StorageHandlerTest {

    private static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static Rest rest; // Wrapper of RestTemplate

    @Autowired
    private ApplicationContext context;


    ObjectMapper objectMapper;
    JrpcRequest jrpcRequest;
    GetByIdParamDto getByIdParamDto;
    GetByListIdParamDto getByListIdParamDto;
    GetAllParamDto getAllParamDto;

    @BeforeAll
    static void setup() {
        TestSuite.INSTANCE.init();
        rest = RestFactory.getRest(true, true);
        rest.setCustomHeader("token", TOKEN);
    }

    @SuppressWarnings("Duplicates")
    @BeforeEach
    void beforeEach() {

        objectMapper = context.getBean(ObjectMapper.class);
        jrpcRequest = context.getBean(JrpcRequest.class);
        getByIdParamDto = context.getBean(GetByIdParamDto.class);
        getByListIdParamDto = context.getBean(GetByListIdParamDto.class);
        getAllParamDto = context.getBean(GetAllParamDto.class);
    }


    @Test
    void getById() throws JsonProcessingException {

        jrpcRequest.setId(22L);

        getByIdParamDto.setId(1L);
        String methodName = GetByIdParamDto.METHOD_NAME;
        jrpcRequest.setMethod(SpringConfiguration.Controller.Handlers.Shop.STORAGE + "." + methodName);
        jrpcRequest.setParams(getByIdParamDto);

        String json = objectMapper.writeValueAsString(jrpcRequest);
        log.info("REQUEST\n" + json);

        ResponseEntity<String> response = rest.post("http://localhost:8084/api", json);
        log.info(response.getStatusCode().toString() + "\n" + response.getBody());
    }

    @Test
    void getByListId() throws JsonProcessingException {


        getByListIdParamDto.setIdList(new ArrayList<>(Arrays.asList(1L, 2L, 3L, 999L)));
        jrpcRequest.setId(22L);

        String methodName = GetByListIdParamDto.METHOD_NAME;
        jrpcRequest.setMethod(SpringConfiguration.Controller.Handlers.Shop.STORAGE + "." + methodName);
        jrpcRequest.setParams(getByListIdParamDto);

        String json = objectMapper.writeValueAsString(jrpcRequest);
        log.info("POST:\n" + json);

        ResponseEntity<String> response = rest.post("http://localhost:8084/api", json);
        log.info(response.getStatusCode().toString() + "\n" + response.getBody());
        log.info("Падать, если к-л из элементов (999L) не найден или отдавать, что нашли без ошибки?");

    }


    @Test
    void getAll() throws JsonProcessingException {

        jrpcRequest.setId(22L);

        String methodName = GetAllParamDto.METHOD_NAME;
        jrpcRequest.setMethod(SpringConfiguration.Controller.Handlers.Shop.STORAGE + "." + methodName);

        String json = objectMapper.writeValueAsString(jrpcRequest);
        log.info("POST:\n" + json);

        ResponseEntity<String> response = rest.post("http://localhost:8084/api", json);
        log.info(response.getStatusCode().toString() + "\n" + response.getBody());

    }

    @Test
    void put() throws JsonProcessingException {

        jrpcRequest.setId(22L);

        String methodName = GetAllParamDto.METHOD_NAME;
        jrpcRequest.setMethod(SpringConfiguration.Controller.Handlers.Shop.STORAGE + "." + methodName);

        String json = objectMapper.writeValueAsString(jrpcRequest);
        log.info("POST:\n" + json);

        ResponseEntity<String> response = rest.post("http://localhost:8084/api", json);
        log.info(response.getStatusCode().toString() + "\n" + response.getBody());

    }

}


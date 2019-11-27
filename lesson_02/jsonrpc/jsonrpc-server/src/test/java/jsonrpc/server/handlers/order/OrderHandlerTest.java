package jsonrpc.server.handlers.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jsonrpc.protocol.dto.base.jrpc.JrpcRequest;
import jsonrpc.protocol.dto.base.param.GetAllParamDto;
import jsonrpc.protocol.dto.base.param.GetByIdParamDto;
import jsonrpc.protocol.dto.base.param.GetByListIdParamDto;
import jsonrpc.protocol.dto.order.OrderDto;
import jsonrpc.protocol.dto.order.OrderItemDto;
import jsonrpc.protocol.dto.order.request.PutOrderParamDto;
import jsonrpc.protocol.dto.product.ProductItemDto;
import jsonrpc.server.TestSuite;
import jsonrpc.server.configuration.ConfigProperties;
import jsonrpc.server.configuration.SpringConfiguration;
import jsonrpc.server.entities.base.mapper.InstantLongMapper;
import jsonrpc.server.entities.base.param.GetByIdParam;
import jsonrpc.server.entities.base.param.GetByIdMapper;
import jsonrpc.server.entities.base.param.GetByIdMapperImpl;
import jsonrpc.server.entities.order.*;
import jsonrpc.server.entities.order.request.PutOrderParam;
import jsonrpc.server.entities.order.request.PutOrderMapperImpl;
import jsonrpc.server.entities.product.ProductItemMapperImpl;
import jsonrpc.server.entities.product.ProductMapperImpl;
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

import static jsonrpc.server.TestSuite.TOKEN;


// Не включишь - не будут прогружаться конфиги из кастомных .properties файлов
// Эту аннотацию надо включать только для тестов
// см https://www.baeldung.com/spring-enable-config-properties
@EnableConfigurationProperties

// не укажешь бины - Spring их и не найдет, явно указываем все требуемые для тестов бины
// (транзистивная зависимость автоматически не разрешается)
@SpringBootTest(classes = {
        SpringConfiguration.class,
        GetByIdParamDto.class,
        GetByListIdParamDto.class,
        GetAllParamDto.class,
        OrderDto.class,
        Order.class,
        PutOrderParamDto.class,
        JrpcRequest.class,
        GetByIdMapperImpl.class,
        ConfigProperties.class})
// Так можно догрузить/переопределить базовые настройки
//@TestPropertySource("classpath:configprops.properties")
class OrderHandlerTest {

    private static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static Rest rest; // Wrapper of RestTemplate

    @Autowired
    private ApplicationContext context;


    ObjectMapper objectMapper;
    JrpcRequest jrpcRequest;
    GetByIdParamDto getByIdParamDto;
    GetByListIdParamDto getByListIdParamDto;
    GetAllParamDto getAllParamDto;
    OrderDto orderDto;
    PutOrderParamDto putOrderParamDto;
    //ProductItemDto productItemDto;


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
        orderDto  = context.getBean(OrderDto.class);
        putOrderParamDto = context.getBean(PutOrderParamDto.class);
    }


    @Test
    void getById() throws JsonProcessingException {

        getByIdParamDto.setId(1L);
        jrpcRequest.setId(22L);

        String methodName = GetByIdParamDto.METHOD_NAME;
        jrpcRequest.setMethod(SpringConfiguration.Controller.Handlers.Shop.ORDER + "." + methodName);
        jrpcRequest.setParams(getByIdParamDto);

        String json = objectMapper.writeValueAsString(jrpcRequest);
        log.info(json);


        ResponseEntity<String> response = rest.post("http://localhost:8084/api", json);
        log.info(response.getStatusCode().toString() + "\n" + response.getBody());
        //System.out.println(response);
    }


    @Test
    void put() throws JsonProcessingException {

        OrderItemDto oiDto = new OrderItemDto();
        oiDto.setCount(3);
        oiDto.setProductId(1L);
        orderDto.addProductItemDto(oiDto);

        // определяем параметры запроса
        putOrderParamDto.setOrder(orderDto);

        jrpcRequest.setId(22L);
        String methodName = PutOrderParamDto.METHOD_NAME;
        jrpcRequest.setMethod(SpringConfiguration.Controller.Handlers.Shop.ORDER + "." + methodName);
        jrpcRequest.setParams(putOrderParamDto);

        String json = objectMapper.writeValueAsString(jrpcRequest);
        log.info(json);

        //json = String.format(json, id);

        ResponseEntity<String> response = rest.post("http://localhost:8084/api", json);
        log.info(response.getStatusCode().toString() + "\n" + response.getBody());
        //System.out.println(response);
    }
}

/*

    @Test
    void getById() throws JsonProcessingException {


        Long id = 33L;
        ObjectMapper objectMapper = context.getBean(ObjectMapper.class);
        //ModelMapper mapper = context.getBean(ModelMapper.class);


//        String json = "{" + TestSuite.INSTANCE.getJrpcHeader() + ", " +
//                      "\"method\": \"shop.entities.order.getById\", " +
//                      "\"params\":{\"id\":\"%1$d\"}" +
//                      "}";
//        json = String.format(json,id);
//        System.out.println(json);

        // Создаем запрос-обертку
        JrpcRequest jrpcRequest = context.getBean(JrpcRequest.class);

        // Создаем запрос
        GetByIdParam getByIdParam = context.getBean(GetByIdParam.class);

        GetByIdMapper getByIdMapper = context.getBean(GetByIdMapper.class);

        // определяем параметры запроса
        getByIdParam.setId(id);
        jrpcRequest.setId(22L);
        //jrpcRequest.setToken("f229fbea-a4b9-40a8-b8ee-e2b47bc1391d");

        String methodName = GetByIdParam.class.getSimpleName();
        methodName = Character.toLowerCase(methodName.charAt(0)) + methodName.substring(1);
        jrpcRequest.setMethod(SpringConfiguration.Controller.Handlers.Shop.ORDER + "." + methodName);
        jrpcRequest.setParams(getByIdMapper.toDto(getByIdParam));

        String json = objectMapper.writeValueAsString(jrpcRequest);

        log.info(json);

        //json = String.format(json, id);

        rest.setCustomHeader("token", "f229fbea-a4b9-40a8-b8ee-e2b47bc1391d");

        ResponseEntity<String> response = rest.post("http://localhost:8084/api", json);
        log.info(response.getStatusCode().toString() + "\n" + response.getBody());
        //System.out.println(response);
    }


    @Test
    void put() throws JsonProcessingException {

        Long id = 33L;

        ObjectMapper objectMapper = context.getBean(ObjectMapper.class);

        // Создаем запрос-обертку
        JrpcRequest jrpcRequest = context.getBean(JrpcRequest.class);


        // Создаем запрос
        // определяем параметры запроса
        OrderDto orderDto = context.getBean(OrderDto.class);

        ProductItemDto piDto = context.getBean(ProductItemDto.class);
        piDto.setCount(3);
        piDto.setProductId(1L);
        orderDto.addProductItemDto(piDto);

        // определяем параметры запроса
        PutOrderParamDto putOrderParamDto = context.getBean(PutOrderParamDto.class);
        putOrderParamDto.setOrderDto(orderDto);

        jrpcRequest.setId(22L);
        //jrpcRequest.setToken("f229fbea-a4b9-40a8-b8ee-e2b47bc1391d");

        String methodName = PutOrderParamDto.METHOD_NAME;
        methodName = Character.toLowerCase(methodName.charAt(0)) + methodName.substring(1);
        jrpcRequest.setMethod(SpringConfiguration.Controller.Handlers.Shop.ORDER + "." + methodName);
        jrpcRequest.setParams(putOrderParamDto);

        String json = objectMapper.writeValueAsString(jrpcRequest);

        log.info(json);

        //json = String.format(json, id);

        rest.setCustomHeader("token", "f229fbea-a4b9-40a8-b8ee-e2b47bc1391d");

        ResponseEntity<String> response = rest.post("http://localhost:8084/api", json);
        log.info(response.getStatusCode().toString() + "\n" + response.getBody());
        //System.out.println(response);
    }
}
 */
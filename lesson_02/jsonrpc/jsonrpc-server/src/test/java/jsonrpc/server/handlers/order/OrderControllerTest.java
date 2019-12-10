package jsonrpc.server.handlers.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import jsonrpc.protocol.dto.base.HandlerName;
import jsonrpc.protocol.dto.base.jrpc.JrpcRequest;
import jsonrpc.protocol.dto.order.OrderDto;
import jsonrpc.protocol.dto.order.OrderItemDto;
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

// Не включишь - не будут прогружаться конфиги из кастомных .properties файлов
// Эту аннотацию надо включать только для тестов
// см https://www.baeldung.com/spring-enable-config-properties
@EnableConfigurationProperties

// не укажешь бины - Spring их и не найдет, явно указываем все требуемые для тестов бины
// (транзистивная зависимость автоматически не разрешается)
@SpringBootTest(classes = {
        BeanConfiguration.class,
        OrderDto.class,
        OrderItemDto.class,
        ConfigProperties.class})
// Так можно догрузить/переопределить базовые настройки
//@TestPropertySource("classpath:configprops.properties")
class OrderControllerTest {

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static Long id;

    @Autowired
    private ApplicationContext context;


    private ObjectMapper objectMapper;
    private JrpcRequest jrpcRequest;
    private Rest rest;

    @BeforeAll
    static void setup() {
        TestSuite.INSTANCE.init();
        id = 1L;
    }


    @SuppressWarnings("Duplicates")
    @BeforeEach
    void beforeEach() {

        rest = context.getBean(Rest.class);
        objectMapper = context.getBean(ObjectMapper.class);
        //jrpcRequest = context.getBean(JrpcRequest.class);
    }


    @Test
    void getById() throws Exception {

        // jrpc id
        jrpcRequest.setId(22L);

        // specify handler and method name, params
        jrpcRequest.setMethod(HandlerName.Order.path + "." + HandlerName.Order.findById);
        jrpcRequest.setParams(objectMapper.valueToTree(id));

        // producing json
        String json = objectMapper.writeValueAsString(jrpcRequest);
        log.info("REQUEST\n" + json);

        // perform request
        ResponseEntity<String> response = rest.post("http://localhost:8084/api", json);
        log.info(response.getStatusCode().toString() + "\n" + response.getBody());
    }


    @Test
    void put() throws Exception {

        jrpcRequest.setId(22L);

        OrderItemDto orderItemDto = context.getBean(OrderItemDto.class);
        orderItemDto.setCount(3);
        orderItemDto.setProductId(3L);
        OrderDto orderDto = context.getBean(OrderDto.class);
        orderDto.addItem(orderItemDto);

        jrpcRequest.setMethod(HandlerName.Order.path + "." + HandlerName.Order.save);
        jrpcRequest.setParams(objectMapper.valueToTree(orderDto));

        String json = objectMapper.writeValueAsString(jrpcRequest);
        log.info("REQUEST\n" + json);

        //json = String.format(json, id);

        ResponseEntity<String> response = rest.post("http://localhost:8084/api", json);
        log.info(response.getStatusCode().toString() + "\n" + response.getBody());
    }

    @Test
    void multi() throws Exception{

        id = 1L;
        getById();
        put();
        id = 1L;
        getById();
        Thread.sleep(2000);
        put();
        id = 2L;
        getById();
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
    void save() throws JsonProcessingException {

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
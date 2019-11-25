package jsonrpc.server.handlers.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jsonrpc.protocol.dto.base.jrpc.JrpcRequest;
import jsonrpc.protocol.dto.order.OrderDto;
import jsonrpc.server.TestSuite;
import jsonrpc.server.configuration.ConfigProperties;
import jsonrpc.server.configuration.SpringConfiguration;
import jsonrpc.server.entities.base.mapper.InstantLongMapper;
import jsonrpc.server.entities.base.param.GetById;
import jsonrpc.server.entities.base.param.GetByIdMapper;
import jsonrpc.server.entities.base.param.GetByIdMapperImpl;
import jsonrpc.server.entities.order.*;
import jsonrpc.server.entities.product.Product;
import jsonrpc.server.entities.product.ProductItemMapperImpl;
import jsonrpc.server.entities.product.ProductMapperImpl;
import jsonrpc.server.utils.Rest;
import jsonrpc.server.utils.RestFactory;
import jsonrpc.server.utils.Utils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.function.Function.identity;


// Не включишь - не будут прогружаться конфиги из кастомных .properties файлов
// Эту аннотацию надо включать только для тестов
// см https://www.baeldung.com/spring-enable-config-properties
@EnableConfigurationProperties

// не укажешь бины - Spring их и не найдет, явно указываем все требуемые для тестов бины
// (транзистивная зависимость автоматически не разрешается)
@SpringBootTest(classes = {
        SpringConfiguration.class,
        GetById.class,
        OrderDto.class,
        Order.class,
        JrpcRequest.class,
        GetByIdMapperImpl.class,
        OrderMapperImpl.class,
        OrderItemMapperImpl.class,
        ProductMapperImpl.class,
        ProductItemMapperImpl.class,
        InstantLongMapper.class,
        ConfigProperties.class})
//@TestPropertySource("classpath:configprops.properties") - можно догрузить/переопределить базовые настройки
class OrderHandlerTest {

    private static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static Rest rest; // Wrapper of RestTemplate

    @Autowired
    private ApplicationContext context;


//    private ObjectMapper objectMapper;
//
//
//    private OrderDto orderDto;
//
//
//    private OrderById orderById;
//
//
//    private JrpcRequest jrpcRequest;


    @BeforeAll
    static void setup() {
        TestSuite.INSTANCE.init();
        rest = RestFactory.getRest(true, true);
    }


    @Test
    void getById() throws JsonProcessingException {


        ConfigProperties configProperties = context.getBean(ConfigProperties.class);


        System.out.println(configProperties.getHostName());



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
        GetById getById = context.getBean(GetById.class);

        GetByIdMapper getByIdMapper = context.getBean(GetByIdMapper.class);

        // определяем параметры запроса
        getById.setId(id);
        jrpcRequest.setId(22L);
        //jrpcRequest.setToken("f229fbea-a4b9-40a8-b8ee-e2b47bc1391d");
        
        String methodName = GetById.class.getSimpleName();
        methodName = Character.toLowerCase(methodName.charAt(0)) + methodName.substring(1);
        jrpcRequest.setMethod(SpringConfiguration.Controller.Handlers.Shop.ORDER + "." + methodName);
        jrpcRequest.setParams(getByIdMapper.toDto(getById));

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
        Order order = context.getBean(Order.class);

        OrderMapper orderMapper = context.getBean(OrderMapper.class);

        // определяем параметры запроса
        OrderItem oi = new OrderItem();
        oi.setOrder(order);
        Product p = new Product();
        Utils.idSetter(p, 1L);
        oi.setProduct(p);
        oi.setCount(4);
        order.addItem(oi);


        jrpcRequest.setId(22L);
        //jrpcRequest.setToken("f229fbea-a4b9-40a8-b8ee-e2b47bc1391d");

        String methodName = "put";
        methodName = Character.toLowerCase(methodName.charAt(0)) + methodName.substring(1);
        jrpcRequest.setMethod(SpringConfiguration.Controller.Handlers.Shop.ORDER + "." + methodName);
        jrpcRequest.setParams(orderMapper.toDto(order));

        String json = objectMapper.writeValueAsString(jrpcRequest);

        log.info(json);

        //json = String.format(json, id);

        rest.setCustomHeader("token", "f229fbea-a4b9-40a8-b8ee-e2b47bc1391d");

        ResponseEntity<String> response = rest.post("http://localhost:8084/api", json);
        log.info(response.getStatusCode().toString() + "\n" + response.getBody());
        //System.out.println(response);
    }
}

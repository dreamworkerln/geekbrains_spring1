package jsonrpc.server.handlers.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jsonrpc.protocol.dto.base.jrpc.JrpcRequest;
import jsonrpc.protocol.dto.order.OrderDto;
import jsonrpc.protocol.dto.base.param.GetById;
import jsonrpc.server.TestSuite;
import jsonrpc.server.entities.Order;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import ru.kvant_telecom.tv.utils.Rest;
import ru.kvant_telecom.tv.utils.RestFactory;

import java.lang.invoke.MethodHandles;


//@RunWith(SpringRunner.class)
//@EnableConfigurationProperties
@SpringBootTest(classes = {ObjectMapper.class, GetById.class,OrderDto.class, Order.class, JrpcRequest.class})
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

        Long id = 33L;



        GetById getById = context.getBean(GetById.class);
        ObjectMapper objectMapper = context.getBean(ObjectMapper.class);


//        String json = "{" + TestSuite.INSTANCE.getJrpcHeader() + ", " +
//                      "\"method\": \"shop.entities.order.getById\", " +
//                      "\"params\":{\"id\":\"%1$d\"}" +
//                      "}";
//        json = String.format(json,id);
//        System.out.println(json);


        getById.setId(id);

        JrpcRequest jrpcRequest = context.getBean(JrpcRequest.class);
        jrpcRequest.setId(22L);
        jrpcRequest.setToken("f229fbea-a4b9-40a8-b8ee-e2b47bc1391d");

        
        String methodName = GetById.class.getSimpleName();
        methodName = Character.toLowerCase(methodName.charAt(0)) + methodName.substring(1);
        jrpcRequest.setMethod("shop.entities.order." + methodName);

        jrpcRequest.setParams(getById);

        String json = objectMapper.writeValueAsString(jrpcRequest);

        System.out.println(json);

        //json = String.format(json, id);

        ResponseEntity<String> response = rest.post("http://localhost:8085/api", json);
        log.info(response.toString());
        //System.out.println(response);
    }
}
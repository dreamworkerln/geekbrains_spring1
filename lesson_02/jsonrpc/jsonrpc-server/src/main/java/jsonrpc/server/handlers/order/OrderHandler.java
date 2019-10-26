package jsonrpc.server.handlers.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jsonrpc.protocol.dto.order.OrderDto;
import jsonrpc.protocol.dto.base.param.GetById;
import jsonrpc.protocol.dto.base.jrpc.JrpcResponse;
import jsonrpc.server.entities.Order;
import jsonrpc.server.handlers.base.JrpcController;
import jsonrpc.server.handlers.base.JrpcHandler;
import jsonrpc.server.handlers.base.MethodHandlerBase;
import jsonrpc.server.repository.OrderRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;

import static jsonrpc.server.configuration.SpringConfiguration.MAIN_ENTITIES_PATH;


@Service
@JrpcController(path = MAIN_ENTITIES_PATH + "." + "order")
public class OrderHandler extends MethodHandlerBase {

    private final static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final OrderRepository orderRepository;

    protected OrderHandler(ObjectMapper objectMapper, ModelMapper modelMapper, OrderRepository orderRepository) {
        super(objectMapper, modelMapper);
        this.orderRepository = orderRepository;
    }


    @JrpcHandler(method = "getById")
    public JrpcResponse getById(JsonNode params) {

        OrderDto result;
        GetById request;

        try {
            request = objectMapper.treeToValue(params, GetById.class);
        } catch (JsonProcessingException e) {
            log.error("json parse error: " + params.toPrettyString(), e);
            throw new IllegalArgumentException(e);
        }

        // Getting from repository order by "id"
        Order order = orderRepository.getById(request.getId());

        try {
            result = convertToDto(order);
        } catch (Exception e) {
            log.error("ModelMapper error", e);
            throw new IllegalArgumentException(e);
        }
        return result;
    }

    // ===============================================



    //https://amydegregorio.com/2018/05/23/skipping-fields-with-modelmapper/
    @Override
    protected void setMappings() {

        modelMapper.addMappings(new PropertyMap<OrderDto, Order>() {
            @Override
            protected void configure() {
                skip().setDate(null);
            }
        });

        modelMapper.addMappings(new PropertyMap<Order, OrderDto>() {
            @Override
            protected void configure() {
                skip().setDate(null);
            }
        });
    }


    // https://www.baeldung.com/entity-to-and-from-dto-for-a-java-spring-application
    // convert DTO from client to Entity



    private OrderDto convertToDto(Order order) {

        OrderDto result = modelMapper.map(order, OrderDto.class);

        result.setDate(order.getDate().getEpochSecond());

        return result;
    }

    private Order convertToEntity(OrderDto orderDto)  {

        Order result = modelMapper.map(orderDto, Order.class);
        return result;
    }


}

package jsonrpc.client.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jsonrpc.client.configuration.ClientProperties;
import jsonrpc.client.request.base.AbstractRequest;
import jsonrpc.protocol.dto.base.HandlerName;
import jsonrpc.protocol.dto.order.OrderDto;
import jsonrpc.protocol.dto.order.OrderItemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;


@Service
public class OrderRequest extends AbstractRequest {


    @Autowired
    public OrderRequest(ApplicationContext context,
                        ObjectMapper objectMapper,
                        ClientProperties clientProperties) {

        super(context, objectMapper, clientProperties);
    }


    public OrderDto findById(long id) throws JsonProcessingException {

        String uri = HandlerName.Order.path + "." + HandlerName.Order.findById;
        JsonNode response = performRequest(1000L, uri, id);
        return objectMapper.treeToValue(response, OrderDto.class);

    }


    public Long save(Long productId, int count) throws JsonProcessingException {

        String uri = HandlerName.Order.path + "." + HandlerName.Order.save;
        OrderItemDto orderItemDto = new OrderItemDto(); //context.getBean("orderItemDto", OrderItemDto.class);
        orderItemDto.setProductId(productId);
        orderItemDto.setCount(count);
        OrderDto order = new OrderDto(); //context.getBean(OrderDto.class);
        order.addItem(orderItemDto);

        JsonNode response = performRequest(1000L, uri, order);
        return objectMapper.treeToValue(response, Long.class);
    }



    public Long save(OrderDto order) throws JsonProcessingException {

        String uri = HandlerName.Order.path + "." + HandlerName.Order.save;
        JsonNode response = performRequest(1000L, uri, order);
        return objectMapper.treeToValue(response, Long.class);
    }
}
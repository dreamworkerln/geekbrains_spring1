package jsonrpc.client.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jsonrpc.client.configuration.ClientProperties;
import jsonrpc.protocol.dto.base.HandlerName;
import jsonrpc.protocol.dto.order.OrderDto;
import jsonrpc.protocol.dto.order.OrderItemDto;
import jsonrpc.protocol.dto.product.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class OrderRequest extends RequestBase {


    @Autowired
    public OrderRequest(ApplicationContext context,
                        ObjectMapper objectMapper,
                        ClientProperties clientProperties) {

        super(context, objectMapper, clientProperties);
    }


    public OrderDto getById(long id) throws JsonProcessingException {

        String uri = HandlerName.Order.path + "." + HandlerName.Order.getById;
        JsonNode response = performRequest(1000L, uri, id);
        return objectMapper.treeToValue(response, OrderDto.class);

    }


    public Long put(Long productId, int count) throws JsonProcessingException {

        String uri = HandlerName.Order.path + "." + HandlerName.Order.put;
        OrderItemDto orderItemDto = context.getBean("orderItemDto", OrderItemDto.class);
        orderItemDto.setProductId(productId);
        orderItemDto.setCount(count);
        OrderDto order = context.getBean(OrderDto.class);
        order.addOrderItemDto(orderItemDto);

        JsonNode response = performRequest(1000L, uri, order);
        return objectMapper.treeToValue(response, Long.class);
    }
}
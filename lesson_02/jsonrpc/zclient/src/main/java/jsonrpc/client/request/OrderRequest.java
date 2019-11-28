package jsonrpc.client.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import jsonrpc.client.configuration.ClientProperties;
import jsonrpc.protocol.dto.base.HandlerName;
import jsonrpc.protocol.dto.base.param.IdDto;
import jsonrpc.protocol.dto.base.param.ListIdDto;
import jsonrpc.protocol.dto.order.OrderItemDto;
import jsonrpc.protocol.dto.product.ProductItemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;


@Service
public class OrderRequest extends RequestBase {


    @Autowired
    public OrderRequest(ApplicationContext context,
                        ObjectMapper objectMapper,
                        ClientProperties clientProperties) {

        super(context, objectMapper, clientProperties);
    }


    public void getById(long id) {

        String uri = HandlerName.Order.path + "." + HandlerName.Order.getById;
        IdDto idDto = context.getBean(IdDto.class);

        idDto.setId(id);
        ResponseEntity<String> response = performRequest(1000L, uri, idDto);
        System.out.println(response.getStatusCode().toString() + "\n" + response.getBody());
    }


    public void put(Long productId, int count) {

        String uri = HandlerName.Order.path + "." + HandlerName.Order.put;
        OrderItemDto orderItemDto = context.getBean("orderItemDto", OrderItemDto.class);
        orderItemDto.setProductId(productId);
        orderItemDto.setCount(count);
        ResponseEntity<String> response = performRequest(1000L, uri, orderItemDto);
        System.out.println(response.getStatusCode().toString() + "\n" + response.getBody());
    }
}
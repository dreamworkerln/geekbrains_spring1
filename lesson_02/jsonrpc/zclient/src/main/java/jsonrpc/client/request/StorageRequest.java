package jsonrpc.client.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import jsonrpc.client.configuration.ClientProperties;
import jsonrpc.protocol.dto.base.HandlerName;
import jsonrpc.protocol.dto.base.jrpc.JrpcRequest;
import jsonrpc.protocol.dto.base.param.IdDto;
import jsonrpc.protocol.dto.base.param.ListIdDto;
import jsonrpc.protocol.dto.order.OrderItemDto;
import jsonrpc.protocol.dto.product.ProductItemDto;
import jsonrpc.utils.Rest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;


@Service
public class StorageRequest extends RequestBase{


    @Autowired
    public StorageRequest(ApplicationContext context,
                          ObjectMapper objectMapper,
                          ClientProperties clientProperties) {

        super(context, objectMapper, clientProperties);
    }


    public void getById(long id) {

        String uri = HandlerName.Storage.path + "." + HandlerName.Storage.getById;
        IdDto idDto = context.getBean(IdDto.class);

        idDto.setId(id);
        ResponseEntity<String> response = performRequest(1000L, uri, idDto);
        System.out.println(response.getStatusCode().toString() + "\n" + response.getBody());
    }


    public void getByListId(long id) {

        String uri = HandlerName.Storage.path + "." + HandlerName.Storage.getByListId;

        ListIdDto listIdDto = context.getBean(ListIdDto.class);
        listIdDto.setList(new ArrayList<>(Arrays.asList(1L, 2L, 3L, 999L)));

        ResponseEntity<String> response = performRequest(1000L, uri, listIdDto);
        System.out.println(response.getStatusCode().toString() + "\n" + response.getBody());
    }


    public void getAll() {

        String uri = HandlerName.Storage.path + "." + HandlerName.Storage.getAll;
        ResponseEntity<String> response = performRequest(1000L, uri, null);
        System.out.println(response.getStatusCode().toString() + "\n" + response.getBody());
    }


    public void put(Long productId, int count) {

        String uri = HandlerName.Storage.path + "." + HandlerName.Storage.put;
        ProductItemDto productItemDto = context.getBean("productItemDto", ProductItemDto.class);
        productItemDto.setProductId(productId);
        productItemDto.setCount(count);
        ResponseEntity<String> response = performRequest(1000L, uri, productItemDto);
        System.out.println(response.getStatusCode().toString() + "\n" + response.getBody());
    }


    public void remove(Long productId, int count) {

        String uri = HandlerName.Storage.path + "." + HandlerName.Storage.remove;
        ProductItemDto productItemDto = context.getBean("productItemDto", ProductItemDto.class);
        productItemDto.setProductId(productId);
        productItemDto.setCount(count);
        ResponseEntity<String> response = performRequest(1000L, uri, productItemDto);
        System.out.println(response.getStatusCode().toString() + "\n" + response.getBody());
    }




}

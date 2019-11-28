package jsonrpc.client.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import jsonrpc.client.configuration.ClientProperties;
import jsonrpc.protocol.dto.base.HandlerName;
import jsonrpc.protocol.dto.base.jrpc.JrpcRequest;
import jsonrpc.protocol.dto.base.param.IdDto;
import jsonrpc.protocol.dto.base.param.ListIdDto;
import jsonrpc.utils.Rest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;


// надеюсь, методы, вызванные одновременно из разных потоков
// будут работать без глюков
@Service
public class ProductRequest extends RequestBase {

    @Autowired
    public ProductRequest(ApplicationContext context,
                          ObjectMapper objectMapper,
                          ClientProperties clientProperties) {

        super(context, objectMapper, clientProperties);
    }


    public void getById(long id) {

        String uri = HandlerName.Product.path + "." + HandlerName.Product.getById;
        IdDto idDto = context.getBean(IdDto.class);
        idDto.setId(id);
        ResponseEntity<String> response = performRequest(1000L, uri, idDto);
        System.out.println(response.getStatusCode().toString() + "\n" + response.getBody());
    }


    public void getByListId(long id) {

        String uri = HandlerName.Product.path + "." + HandlerName.Product.getByListId;

        ListIdDto listIdDto = context.getBean(ListIdDto.class);
        listIdDto.setList(new ArrayList<>(Arrays.asList(1L, 2L, 3L, 999L)));

        ResponseEntity<String> response = performRequest(1000L, uri, listIdDto);
        System.out.println(response.getStatusCode().toString() + "\n" + response.getBody());
    }


    public void getAll() {

        String uri = HandlerName.Product.path + "." + HandlerName.Product.getAll;
        ResponseEntity<String> response = performRequest(1000L, uri, null);
        System.out.println(response.getStatusCode().toString() + "\n" + response.getBody());
    }
}

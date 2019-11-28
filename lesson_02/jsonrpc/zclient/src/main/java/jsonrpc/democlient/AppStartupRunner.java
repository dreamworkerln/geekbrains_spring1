package jsonrpc.democlient;

import com.fasterxml.jackson.databind.ObjectMapper;
import jsonrpc.protocol.dto.base.HandlerName;
import jsonrpc.protocol.dto.base.jrpc.JrpcRequest;
import jsonrpc.protocol.dto.base.param.IdDto;
import jsonrpc.protocol.dto.base.param.IdListDto;
import jsonrpc.utils.Rest;
import jsonrpc.utils.RestFactory;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Component
public class AppStartupRunner implements ApplicationRunner {

    private static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ApplicationContext context;
    private final ObjectMapper objectMapper;


    @Autowired
    public AppStartupRunner(ApplicationContext context, ObjectMapper objectMapper) {
        this.context = context;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        getAll();
    }





    void getById(long id) throws Exception {

        Rest rest = context.getBean(Rest.class);

        JrpcRequest jrpcRequest = context.getBean(JrpcRequest.class);
        IdDto idDto = context.getBean(IdDto.class);
        jrpcRequest.setId(1000L);

        idDto.setId(id);
        jrpcRequest.setMethod(HandlerName.Storage.path + "." + HandlerName.Storage.getById);
        jrpcRequest.setParams(idDto);

        String json = objectMapper.writeValueAsString(jrpcRequest);
        System.out.println("REQUEST\n" + json);

        ResponseEntity<String> response = rest.post("http://localhost:8084/api", json);
        System.out.println(response.getStatusCode().toString() + "\n" + response.getBody());
    }


    void getByListId(long id) throws Exception {

        Rest rest = context.getBean(Rest.class);

        JrpcRequest jrpcRequest = context.getBean(JrpcRequest.class);
        IdListDto idListDto = context.getBean(IdListDto.class);

        // See implementation in tests: jsonrpc.server.handlers.storage.StorageHandlerTest

        throw new NotImplementedException("...");
    }


    void getAll() throws Exception {

        Rest rest = context.getBean(Rest.class);

        JrpcRequest jrpcRequest = context.getBean(JrpcRequest.class);
        jrpcRequest.setId(1000L);

        jrpcRequest.setMethod(HandlerName.Storage.path + "." + HandlerName.Storage.getAll);
        String json = objectMapper.writeValueAsString(jrpcRequest);
        System.out.println("REQUEST:\n" + json);

        ResponseEntity<String> response = rest.post("http://localhost:8084/api", json);
        System.out.println(response.getStatusCode().toString() + "\n" + response.getBody());
    }


}

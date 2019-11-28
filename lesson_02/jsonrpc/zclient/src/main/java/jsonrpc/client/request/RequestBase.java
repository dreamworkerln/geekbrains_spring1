package jsonrpc.client.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jsonrpc.client.configuration.ClientProperties;
import jsonrpc.protocol.dto.base.HandlerName;
import jsonrpc.protocol.dto.base.jrpc.AbstractDto;
import jsonrpc.protocol.dto.base.jrpc.JrpcRequest;
import jsonrpc.protocol.dto.base.param.IdDto;
import jsonrpc.utils.Rest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Component
@Scope("prototype")
public abstract class RequestBase {

    private static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    protected final ApplicationContext context;
    protected final ObjectMapper objectMapper;
    protected final ClientProperties clientProperties;

    protected final String apiURL;

    public RequestBase(ApplicationContext context, ObjectMapper objectMapper, ClientProperties clientProperties) {

        this.context = context;
        this.objectMapper = objectMapper;
        this.clientProperties = clientProperties;

        apiURL = String.format("http://%1$s:%2$s/api",
                this.clientProperties.getServer().getHostName(),
                this.clientProperties.getServer().getPort());
    }


    ResponseEntity<String> performRequest(long id, String uri, AbstractDto params) {

        JrpcRequest jrpcRequest = context.getBean(JrpcRequest.class);
        jrpcRequest.setMethod(uri);
        jrpcRequest.setId(id);
        jrpcRequest.setParams(params);

        String json;
        try {
            json = objectMapper.writeValueAsString(jrpcRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        log.warn("REQUEST\n" + json);
        Rest rest = context.getBean(Rest.class);
        return rest.post(apiURL, json);
    }


}

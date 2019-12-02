package jsonrpc.client.request.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jsonrpc.client.configuration.ClientProperties;
import jsonrpc.protocol.dto.base.jrpc.JrpcRequest;
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
public abstract class AbstractRequest {

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    protected final ApplicationContext context;
    protected final ObjectMapper objectMapper;
    protected final ClientProperties clientProperties;

    final String apiURL;

    public AbstractRequest(ApplicationContext context, ObjectMapper objectMapper, ClientProperties clientProperties) {

        // Контекст нужен, т.к. некоторые бины (JrpcRequest и  Rest) имеют scope == prototype
        this.context = context;
        this.objectMapper = objectMapper;
        this.clientProperties = clientProperties;

        apiURL = String.format("http://%1$s:%2$s/api",
                this.clientProperties.getServer().getHostName(),
                this.clientProperties.getServer().getPort());
    }


    protected JsonNode performRequest(long id, String uri, Object params) {

        JsonNode result;

        // JrpcRequest это прототип, нельзя @Autowire
        JrpcRequest jrpcRequest = context.getBean(JrpcRequest.class);
        jrpcRequest.setMethod(uri);
        jrpcRequest.setId(id);
        jrpcRequest.setParams(objectMapper.valueToTree(params));

        String json;
        try {
            json = objectMapper.writeValueAsString(jrpcRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        log.warn("REQUEST\n" + json);
        Rest rest = context.getBean(Rest.class);
        ResponseEntity<String> response = rest.post(apiURL, json);

        //System.out.println(response.getHeaders().toString());


        log.warn("HTTP " + response.getStatusCode().toString() + "\n" + response.getBody());
        try {
            result = objectMapper.readTree(response.getBody()).get("result");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return result;
    }


}

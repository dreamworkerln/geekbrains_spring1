package jsonrpc.client.request.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jsonrpc.client.configuration.ClientProperties;
import jsonrpc.protocol.jrpc.request.JrpcRequest;
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

    //private final static String JRPC_VERSION = "2.0";
    private final static String API_VERSION = "1.0";


    private final ApplicationContext context;
    private final ClientProperties clientProperties;
    protected final ObjectMapper objectMapper;

    private final String apiURL;

    public AbstractRequest(ApplicationContext context, ObjectMapper objectMapper, ClientProperties clientProperties) {

        // Контекст нужен, т.к. некоторые бины (JrpcRequest и  Rest) имеют scope == prototype
        this.context = context;
        this.objectMapper = objectMapper;
        this.clientProperties = clientProperties;

        apiURL = String.format("http://%1$s:%2$s/api/%3$s/",
                this.clientProperties.getServer().getHostName(),
                this.clientProperties.getServer().getPort(),
                API_VERSION);
    }


    protected JsonNode performRequest(long id, String uri, Object params) {

        JsonNode result;

        // JrpcRequest это прототип, нельзя @Autowire
        JrpcRequest jrpcRequest = new JrpcRequest(); //context.getBean(JrpcRequest.class);
        jrpcRequest.setMethod(uri);
        jrpcRequest.setId(id);
        jrpcRequest.setParams(objectMapper.valueToTree(params));

        String json;
        try {
            json = objectMapper.writeValueAsString(jrpcRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        log.info("REQUEST\n" + json);
        Rest rest = context.getBean(Rest.class);
        ResponseEntity<String> response = rest.post(apiURL, json);

        //System.out.println(response.getHeaders().toString());


        log.info("HTTP " + response.getStatusCode().toString() + "\n" + response.getBody());
        try {
            result = objectMapper.readTree(response.getBody()).get("result");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return result;
    }


}

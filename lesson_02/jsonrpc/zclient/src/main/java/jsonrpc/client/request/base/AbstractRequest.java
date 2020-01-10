package jsonrpc.client.request.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import jsonrpc.client.configuration.ClientProperties;
import jsonrpc.protocol.http.GrantType;
import jsonrpc.protocol.http.OauthResponse;
import jsonrpc.protocol.jrpc.request.JrpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.security.Key;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicReference;

@Component
@Scope("prototype")
public abstract class AbstractRequest {

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    //private final static String JRPC_VERSION = "2.0";
    private final static String API_VERSION = "1.0";


    private ApplicationContext context;
    private ClientProperties clientProperties;
    private RestTemplate restTemplate;
    private OauthRequest oauthRequest;


    protected ObjectMapper objectMapper;



    private String apiURL;

    @Autowired
    public void setContext(ApplicationContext context) {
        this.context = context;
    }

    @Autowired
    public void setClientProperties(ClientProperties clientProperties) {
        this.clientProperties = clientProperties;
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Autowired
    public void setOauthRequest(OauthRequest oauthRequest) {
        this.oauthRequest = oauthRequest;
    }

    @PostConstruct
    private void postConstruct() {
        apiURL = String.format("http://%1$s:%2$s/api/%3$s/",
                this.clientProperties.getResourceServer().getHostName(),
                this.clientProperties.getResourceServer().getPort(),
                API_VERSION);

    }

    // --------------------------------------------------------------------



    protected JsonNode performRequest(long id, String uri, Object params) {

        ClientProperties.Credentials clientCredentials = clientProperties.getCredentials();

        // Oauth2.0 authorization -------------------------------------------

        // если устарел access_token или refresh_token
        if (clientCredentials.getAccessToken().isRotten() ||
            clientCredentials.getRefreshToken().isRotten()) {


            // Если refresh_token не протух
            if (!clientCredentials.getRefreshToken().isRotten()) {
                // refresh refresh_token
                // получим заодно новый access_token
                oauthRequest.refreshToken();
            } else {

                // get token (really get only reduced functionality 1 refresh token - waiting to token have been approved)
                oauthRequest.obtainToken();

                // simulate approving this refresh token from "confidential client (maybe from mobile app)"
                oauthRequest.approve(clientCredentials.getRefreshToken().getId());

                // then get fully functional access+refresh token pair with normal access level
                oauthRequest.refreshToken();
            }

        }

        JsonNode result;

        // JrpcRequest не был запихнут в @Bean //context.getBean(JrpcRequest.class); поэтому new()
        JrpcRequest jrpcRequest = new JrpcRequest();
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

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + clientCredentials.getAccessToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        RequestEntity<String> requestEntity = RequestEntity
                .post(URI.create(apiURL))
                .headers(headers)
                .body(json);

        ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

        log.info("HTTP " + response.getStatusCode().toString() + "\n" + response.getBody());
        try {
            result = objectMapper.readTree(response.getBody()).get("result");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return result;
    }


}

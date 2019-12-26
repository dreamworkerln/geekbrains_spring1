package jsonrpc.client.request.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jsonrpc.client.configuration.ClientProperties;
import jsonrpc.protocol.http.OauthResponse;
import jsonrpc.protocol.jrpc.request.JrpcRequest;
import jsonrpc.utils.Rest;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.time.Instant;

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
                this.clientProperties.getResourceServer().getHostName(),
                this.clientProperties.getResourceServer().getPort(),
                API_VERSION);
    }


    protected JsonNode performRequest(long id, String uri, Object params) {

        ClientProperties.Credentials clientCredentials = clientProperties.getCredentials();

        // Oauth2.0 authorization -------------------------------------------
        if (clientCredentials.getAccessToken() == null ||
            clientCredentials.getRefreshToken() == null) {

            log.info("OAUTH get TOKEN");

            obtainToken();
        }
        else if (clientCredentials.getObtained().getEpochSecond() + clientCredentials.getAccessTokenExpire() <=
                Instant.now().getEpochSecond()) {

            log.info("OAUTH refresh TOKEN");
            refreshToken();
        }
        // --------------------------------------------------------------------


        JsonNode result;

        // JrpcRequest не был запихнут в @Bean //context.getBean(JrpcRequest.class);
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
        Rest<String> rest = new Rest<>(10000);
        rest.setCustomHeader("Authorization", "Bearer " + clientCredentials.getAccessToken());

        ResponseEntity<String> response = rest.post(apiURL, json);

        log.info("HTTP " + response.getStatusCode().toString() + "\n" + response.getBody());
        try {
            result = objectMapper.readTree(response.getBody()).get("result");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return result;
    }




    private void obtainTokenAbstract(String grantType) {

        if(!grantType.equals("password") && !grantType.equals("refresh_token")) {
            throw new IllegalArgumentException("grantType not recognized");
        }


        ClientProperties.Credentials clientCredentials = clientProperties.getCredentials();

        Rest<OauthResponse> rest = new Rest<>(10000);


        rest.setCredentials(new UsernamePasswordCredentials(
                clientCredentials.getClientId(),
                clientCredentials.getClientSecret()));

        String params = String.format("grant_type=%1$s&username=%2$s&password=%3$s",
                grantType,
                clientCredentials.getUsername(),
                clientCredentials.getPassword());




        //rest.setCustomHeader("grant_type", grantType);
        //rest.setCustomHeader("username", clientCredentials.getUsername());
        //rest.setCustomHeader("password", clientCredentials.getPassword());

        String getTokenURL = String.format("http://%1$s:%2$s/oauth/token?%3$s",
                this.clientProperties.getAuthServer().getHostName(),
                this.clientProperties.getAuthServer().getPort(),
                params);

        ResponseEntity<OauthResponse> response = rest.post(getTokenURL, OauthResponse.class);

        OauthResponse oauthResponse = response.getBody();
        clientCredentials.setAccessToken(oauthResponse.getAccess_token());
        clientCredentials.setRefreshToken(oauthResponse.getAccess_token());
        clientCredentials.setAccessTokenExpire(oauthResponse.getExpires_in());
        clientCredentials.setObtained(Instant.now());
    }
    

    private void obtainToken() {
        obtainTokenAbstract("password");
    }

    private void refreshToken() {
        obtainTokenAbstract("refresh_token");
    }

}

package jsonrpc.client.request.admin;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import jsonrpc.client.configuration.ClientProperties;
import jsonrpc.client.request.base.AbstractRequest;
import jsonrpc.client.request.base.OauthRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.lang.invoke.MethodHandles;
import java.net.URI;

@Component
public class AdminRequest extends AbstractRequest {

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ClientProperties clientProperties;
    private final RestTemplate restTemplate;
    private final OauthRequest oauthRequest;
    private final ObjectMapper objectMapper;

    public AdminRequest(ClientProperties clientProperties, RestTemplate restTemplate, OauthRequest oauthRequest, ObjectMapper objectMapper) {
        this.clientProperties = clientProperties;
        this.restTemplate = restTemplate;
        this.oauthRequest = oauthRequest;
        this.objectMapper = objectMapper;
    }



    protected <K, T> ResponseEntity<T> performRequest(String uri, K body, Class<T> returnClass) {

        ClientProperties.Credentials clientCredentials = clientProperties.getCredentials();

        // Oauth2.0 authorization -----------------
        oauthRequest.authorize();



        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + clientCredentials.getAccessToken());
        //headers.setContentType(MediaType.APPLICATION_JSON);

        RequestEntity<K> requestEntity = RequestEntity
            .post(URI.create(uri))
            .headers(headers)
            .body(body);

        log.info("REQUEST\n" + requestEntity);

        ResponseEntity<T> response = restTemplate.exchange(requestEntity, returnClass);

        //log.info("HTTP " + response.getStatusCode().toString() + "\n" + response.getBody());

        return response;
    }



    public void revokeToken(String userName) {

        // Oauth2.0 authorization
        oauthRequest.authorize();

        String url = String.format("http://%1$s:%2$s/admin/user/revoke_token",
            this.clientProperties.getAuthServer().getHostName(),
            this.clientProperties.getAuthServer().getPort());

        //ObjectNode body = JsonNodeFactory.instance.objectNode();
        //body.put("user", userName);
        ResponseEntity<Void> response = performRequest(url, userName, Void.class);

        log.info("{}", response.getStatusCode());
    }

}

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

    @PostConstruct
    private void postConstruct() {
        apiURL = String.format("http://%1$s:%2$s/api/%3$s/",
                this.clientProperties.getResourceServer().getHostName(),
                this.clientProperties.getResourceServer().getPort(),
                API_VERSION);

    }





    protected JsonNode performRequest(long id, String uri, Object params) {

        ClientProperties.Credentials clientCredentials = clientProperties.getCredentials();

        // Oauth2.0 authorization -------------------------------------------
        if (clientCredentials.getAccessToken() == null ||clientCredentials.getRefreshToken() == null) {

            if (clientCredentials.getRefreshToken() != null) {
                refreshToken();
            }

            // get tokens (really get only reduced functionality 1 refresh token - waiting to token have been approved)
            obtainToken();

            // simulate approving this refresh token from "confidential client (maybe from mobile app)"
            approve(clientCredentials.getRefreshId());

            // then get fully functional access+refresh tokens pair with normal access level
            refreshToken();


        }
        else if (clientCredentials.getAccessTokenExpiration().toEpochMilli() < Instant.now().toEpochMilli()) {

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




    private void obtainTokenAbstract(GrantType grantType) {

        ClientProperties.Credentials clientCredentials = clientProperties.getCredentials();

        String params = String.format("grant_type=%1$s", grantType.getValue());

        String getTokenURL = String.format("http://%1$s:%2$s/oauzz/token/get",
                this.clientProperties.getAuthServer().getHostName(),
                this.clientProperties.getAuthServer().getPort());


        RequestEntity<String> requestEntity = null;
        if (grantType == GrantType.PASSWORD) {


            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setBasicAuth(clientCredentials.getUsername(), clientCredentials.getPassword());
            requestEntity = RequestEntity
                    .post(URI.create(getTokenURL))
                    .headers(headers)
                    .body(params);
        }
        else if (grantType == GrantType.REFRESH) {

            String authorization = "Bearer " + clientCredentials.getRefreshToken();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authorization);
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            requestEntity = RequestEntity
                    .post(URI.create(getTokenURL))
                    .headers(headers)
                    .body(params);
        }

        assert requestEntity != null;
        ResponseEntity<OauthResponse> response = restTemplate.exchange(requestEntity, OauthResponse.class);

        OauthResponse oauthResponse = response.getBody();
        clientCredentials.setAccessToken(oauthResponse.getAccessToken());
        clientCredentials.setRefreshToken(oauthResponse.getRefreshToken());
        clientCredentials.setObtained(Instant.now());

        Claims claims = getClaims(oauthResponse.getAccessToken());

        if (claims != null) {
            clientCredentials.setAccessTokenExpiration(claims.getExpiration().toInstant());
        }

        claims = getClaims(oauthResponse.getRefreshToken());
        if (claims != null) {
            clientCredentials.setRefreshId(Long.valueOf(claims.getId()));
        }



        log.info("access_token: {}", clientCredentials.getAccessToken());
        log.info("access_token expiration: {}", clientCredentials.getAccessTokenExpiration());
        log.info("refresh_token: {}", clientCredentials.getRefreshToken());

    }


    private boolean checkTokenApproval() {

        ClientProperties.Credentials clientCredentials = clientProperties.getCredentials();

        String authorization = "Bearer " + clientCredentials.getRefreshToken();

        String checkTokenURL = String.format("http://%1$s:%2$s/oauzz/token/check_approved",
                this.clientProperties.getAuthServer().getHostName(),
                this.clientProperties.getAuthServer().getPort());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorization);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        RequestEntity requestEntity = RequestEntity
                .post(URI.create(checkTokenURL))
                .headers(headers)
                .build();

        ResponseEntity<OauthResponse> response = restTemplate.exchange(requestEntity, OauthResponse.class);

        return response.getStatusCode() == HttpStatus.OK;
    }



    public void approve(Long id) {

        ClientProperties.Credentials clientCredentials = clientProperties.getCredentials();

        log.info("APPROVING TOKEN id={}", id);

        String approveTokenURL = String.format("http://%1$s:%2$s/oauzz/token/approve",
                this.clientProperties.getAuthServer().getHostName(),
                this.clientProperties.getAuthServer().getPort());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(clientCredentials.getUsername(), clientCredentials.getPassword());

        RequestEntity<String> requestEntity = RequestEntity
                .post(URI.create(approveTokenURL))
                .headers(headers)
                .body("id=" + id);

        ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
        log.info("{}", response.getStatusCode());
    }





    private void obtainToken() {
        log.info("OAUTH GET TOKEN");
        obtainTokenAbstract(GrantType.PASSWORD);
    }



    private void refreshToken() {
        log.info("OAUTH REFRESH TOKEN");

        // check that token is approved
        if (!checkTokenApproval()) {
            String s = "OAUTH REFRESH TOKEN NOT APPROVED";
            log.error(s);
            throw new RuntimeException(s);
        }

        obtainTokenAbstract(GrantType.REFRESH);
    }



    // get claims without checking key signing
    private Claims getClaims(String token) {

        final AtomicReference<Claims> result = new AtomicReference<>();

        SigningKeyResolver signingKeyResolver = new SigningKeyResolverAdapter() {
            @Override
            public Key resolveSigningKey(JwsHeader header, Claims claims) {
                result.set(claims);
                // Examine header and claims
                return null; // will throw exception, can be caught in caller
            }
        };

        try {

            Jwts.parser()
                    .setSigningKeyResolver(signingKeyResolver)
                    .parseClaimsJws(token)
                    .getBody();
        }
        catch (Exception e) {
            // no signing key on client. 
            // We trust that this JWT came from the trusted server and has been verified(issued) there.
        }
        return result.get();
    }

}

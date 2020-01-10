package jsonrpc.client.request.base;

import jsonrpc.client.configuration.ClientProperties;
import jsonrpc.protocol.http.BlackListResponse;
import jsonrpc.protocol.token.GrantType;
import jsonrpc.protocol.http.OauthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.lang.invoke.MethodHandles;
import java.net.URI;

@Component
@Scope("prototype")
public class OauthRequest {

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private RestTemplate restTemplate;
    private ClientProperties clientProperties;


    @Autowired
    public void setClientProperties(ClientProperties clientProperties) {
        this.clientProperties = clientProperties;
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }





    private void obtainTokenAbstract(GrantType grantType) {

        ClientProperties.Credentials clientCredentials = clientProperties.getCredentials();

        //String params = String.format("grant_type=%1$s", grantType.getValue());

        String getTokenURL = String.format("http://%1$s:%2$s/oauzz/token/%3$s",
            this.clientProperties.getAuthServer().getHostName(),
            this.clientProperties.getAuthServer().getPort(),
            grantType == GrantType.PASSWORD ? "/get" : "/refresh"
        );


        RequestEntity requestEntity = null;
        if (grantType == GrantType.PASSWORD) {


            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setBasicAuth(clientCredentials.getUsername(), clientCredentials.getPassword());
            requestEntity = RequestEntity
                .post(URI.create(getTokenURL))
                .headers(headers)
                .build();

        }
        else if (grantType == GrantType.REFRESH) {

            String authorization = "Bearer " + clientCredentials.getRefreshToken();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authorization);
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            requestEntity = RequestEntity
                .post(URI.create(getTokenURL))
                .headers(headers)
                .build();
        }

        assert requestEntity != null;
        ResponseEntity<OauthResponse> response = restTemplate.exchange(requestEntity, OauthResponse.class);

        OauthResponse oauthResponse = response.getBody();

        clientCredentials.setAccessToken(new MyToken(oauthResponse.getAccessToken()));
        clientCredentials.setRefreshToken(new MyToken(oauthResponse.getRefreshToken()));

        log.info("access_token: {}", clientCredentials.getAccessToken());
        log.info("access_token expiration: {}", clientCredentials.getAccessToken().getExpiration());
        log.info("refresh_token: {}", clientCredentials.getRefreshToken());
        log.info("refresh_token expiration: {}", clientCredentials.getRefreshToken().getExpiration());

    }


    private boolean checkTokenApproval() {

        ClientProperties.Credentials clientCredentials = clientProperties.getCredentials();

        String authorization = "Bearer " + clientCredentials.getRefreshToken();

        String checkTokenURL = String.format("http://%1$s:%2$s/oauzz/token/check_is_approved",
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





    public void obtainToken() {
        log.info("OAUTH GET TOKEN");
        obtainTokenAbstract(GrantType.PASSWORD);
    }



    public void refreshToken() {
        log.info("OAUTH REFRESH TOKEN");

        // check that token is approved
        if (!checkTokenApproval()) {
            String s = "OAUTH REFRESH TOKEN NOT APPROVED";
            log.error(s);
            throw new RuntimeException(s);
        }

        obtainTokenAbstract(GrantType.REFRESH);
    }



    public BlackListResponse getBlackList(Long from) {

        ClientProperties.Credentials clientCredentials = clientProperties.getCredentials();

        String checkTokenURL = String.format("http://%1$s:%2$s/oauzz/token/listblack",
            this.clientProperties.getAuthServer().getHostName(),
            this.clientProperties.getAuthServer().getPort());

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(clientCredentials.getUsername(), clientCredentials.getPassword());
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        RequestEntity requestEntity = RequestEntity
            .post(URI.create(checkTokenURL))
            .headers(headers)
            .body("from=" + from);

        ResponseEntity<BlackListResponse> response =
            restTemplate.exchange(requestEntity, BlackListResponse.class);

        return response.getBody();
    }

}

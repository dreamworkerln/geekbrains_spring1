package jsonrpc.client.request;

import jsonrpc.client.configuration.ClientProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.invoke.MethodHandles;
import java.net.URI;

@Service
public class TokenAdmin {

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final static String API_VERSION = "1.0";

    private final ClientProperties clientProperties;
    private final RestTemplate restTemplate;

    private String apiURL;

    @Autowired
    public TokenAdmin(ClientProperties clientProperties, RestTemplate restTemplate) {
        this.clientProperties = clientProperties;
        this.restTemplate = restTemplate;

        apiURL = String.format("http://%1$s:%2$s/",
                this.clientProperties.getAuthServer().getHostName(),
                this.clientProperties.getAuthServer().getPort());
    }

    public void approve(Long id) {

        ClientProperties.Credentials clientCredentials = clientProperties.getCredentials();

        log.info("APPROVING TOKEN id={}", id);

        String getTokenURL = apiURL + "admin/token/approve";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(clientCredentials.getUsername(), clientCredentials.getPassword());

        RequestEntity<String> requestEntity = RequestEntity
                .post(URI.create(getTokenURL))
                .headers(headers)
                .body("id=" + id);

        ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
        log.info("{}", response.getStatusCode());
    }
}

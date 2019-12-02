package jsonrpc.utils;

//import org.apache.commons.codec.binary.Base64;
//import org.apache.http.auth.Credentials;
//import org.apache.http.client.config.RequestConfig;
//import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.ssl.TrustStrategy;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.auth.Credentials;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;


/**
 * RestTemplate Error handler - by default only suppress exception (client and server)
 */
class RestTemplateResponseErrorHandler
        implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse httpResponse)
            throws IOException {

        return (
                httpResponse.getStatusCode().series() == CLIENT_ERROR||
                httpResponse.getStatusCode().series() == SERVER_ERROR);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public void handleError(ClientHttpResponse httpResponse)throws IOException {


        if (httpResponse.getStatusCode().series() == SERVER_ERROR) {
            // handle SERVER_ERROR

        }
        else if (httpResponse.getStatusCode().series() == CLIENT_ERROR) {
            // handle CLIENT_ERROR

            /*
            reading response as string

            String s = new BufferedReader(new InputStreamReader(httpResponse.getBody()))
                    .lines().collect(Collectors.joining());

            System.out.println(s);
            throw new RuntimeException(s);

            if (httpResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
                //throw new NotFoundException("Not found");
            }
            */
        }
    }
}


class RestTemplateFactory {

    static RestTemplate getRestTemplate(boolean checkCert, boolean throwOnError, int timeout) {

        HttpComponentsClientHttpRequestFactory requestFactory;

        RestTemplate result;

        if (!checkCert) {


            try {

                TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

                SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
                        .loadTrustMaterial(null, acceptingTrustStrategy)
                        .build();


                RequestConfig requestConfig = RequestConfig.custom()
                        .setConnectTimeout(timeout)
                        .setConnectionRequestTimeout(timeout)
                        .setSocketTimeout(timeout)
                        .build();


                SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
                CloseableHttpClient httpClient = HttpClients.custom()
                        .setDefaultRequestConfig(requestConfig)
                        .setSSLSocketFactory(csf)
                        .build();

                requestFactory =
                        new HttpComponentsClientHttpRequestFactory();

                requestFactory.setConnectTimeout(timeout);
                requestFactory.setConnectionRequestTimeout(timeout);
                requestFactory.setReadTimeout(timeout);
                requestFactory.setHttpClient(httpClient);

                result = new RestTemplate(requestFactory);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        else {
            HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
            httpRequestFactory.setConnectionRequestTimeout(timeout);
            httpRequestFactory.setConnectTimeout(timeout);
            httpRequestFactory.setReadTimeout(timeout);

            result = new RestTemplate(httpRequestFactory);
        }

        // Force using UTF-8 as default character encoding
        result.getMessageConverters()
                .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

        // Add custom errorHandler that suppress any exceptions
        if (!throwOnError) {
            result.setErrorHandler(new RestTemplateResponseErrorHandler());
        }

        return result;
    }

}



// ------------------------------------------------------------------------------------------

/**
 * Bult-in curl, allowed enabling/disabling ssl(tls) сertificates check and custom timeouts(default 5000 ms)
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class Rest {

    protected int timeout = 5000;

    protected String userAgent = "ass-with-ears-2.0";

    protected Credentials credentials = null;

    protected boolean checkCert = true;

    protected boolean throwOnError = true;

    protected RestTemplate restTemplate;

    protected Map<String, String> customHeaders = new HashMap<>();

    protected Rest() {

        restTemplate = RestTemplateFactory.getRestTemplate(checkCert, throwOnError, timeout);
    }

    protected Rest(int timeout) {

        this.timeout = timeout;
        restTemplate = RestTemplateFactory.getRestTemplate(checkCert, throwOnError, timeout);
    }

    protected Rest(boolean throwOnError, int timeout) {

        this.timeout = timeout;
        restTemplate = RestTemplateFactory.getRestTemplate(checkCert, throwOnError, timeout);
    }


    protected Rest(boolean checkCert, boolean throwOnError) {

        this.checkCert = checkCert;
        restTemplate = RestTemplateFactory.getRestTemplate(checkCert, throwOnError, timeout);
    }

    protected Rest(boolean checkCert, boolean throwOnError, int timeout) {

        this.timeout = timeout;
        this.checkCert = checkCert;
        restTemplate = RestTemplateFactory.getRestTemplate(checkCert, throwOnError, timeout);
    }

    //






    /**
     * Perform get request
     * @param url Url
     * @return ResponseEntity<String>
     */
    public ResponseEntity<String> get(String url) {

        ResponseEntity<String> result;

        //Set the headers you need send
        final HttpHeaders headers = getHeaders();
        //headers.set("User-Agent", userAgent);

        //Create a new HttpEntity
        HttpEntity<String> entity = new HttpEntity<>(headers);

        result =  restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return result;
    }


    /**
     * Perform get request
     * @param url Url
     * @return ResponseEntity<byte[]>
     */
    public ResponseEntity<byte[]> download(String url) {

        ResponseEntity<byte[]> result;

        HttpHeaders headers = getHeaders();
        //headers.set("User-Agent", userAgent);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_OCTET_STREAM));

        HttpEntity<String> entity = new HttpEntity<>(headers);
        result = restTemplate.exchange(url, HttpMethod.GET, entity, byte[].class);

        return result;
    }



    /**
     *
     * @param url Url
     * @param data String
     * @return ResponseEntity<String>
     */
    public ResponseEntity<String> post(String url, String data) {

        ResponseEntity<String> result;

        //Set the headers you need send
        HttpHeaders headers = getHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        //Create a new HttpEntity
        HttpEntity<String> entity = new HttpEntity<>(data, headers);

        result = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        return result;
    }


    public void setCustomHeader(String name, String value) {

        customHeaders.put(name, value);
    }


    // -------------------------------------------------------------------------


    protected HttpHeaders getHeaders() {

        HttpHeaders result = new HttpHeaders();

        // first apply custom headers
        customHeaders.forEach(result::set);

        // then add User-Agent
        result.set("User-Agent", userAgent);

        //https://www.baeldung.com/how-to-use-resttemplate-with-basic-authentication-in-spring
        if (credentials != null) {

            String auth = credentials.getUserPrincipal().getName() + ":" + credentials.getPassword();
            String encodedAuth = Base64.encodeBase64String(auth.getBytes(Charset.forName("US-ASCII")));

            String authHeader = "Basic " + encodedAuth;
            result.set("Authorization", authHeader);

            //byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")) );
            //String authHeader = "Basic " + new String(encodedAuth);  "YWRtaW46YWRtaW4=";
        }
        
        return result;
    }



    // -------------------------------------------------------------------------


    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }
}

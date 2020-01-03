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
 * RestTemplate Error handler - by default only suppress exception (client and resourceserver)
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


// ------------------------------------------------------------------------------------------

/**
 * Bult-in curl, allowed enabling/disabling ssl(tls) сertificates check and custom timeouts(default 5000 ms)
 */
@SuppressWarnings({"WeakerAccess", "unused"})
class Rest {

/*    protected static final int TIMEOUT = 5000;

    // protected String userAgent = "ass-with-ears-2.0";

    //protected Credentials credentials = null;

    protected static final boolean CHECK_SERT = true;

    protected static final boolean THROW_ON_ERROR = true;

    // protected RestTemplate restTemplate;

    //protected Map<String, String> customHeaders = new HashMap<>();

    public RestTemplate getRestTemplate() {

        return RestTemplateFactory.getRestTemplate(CHECK_SERT, THROW_ON_ERROR, TIMEOUT);
    }

    public RestTemplate getRestTemplate(int timeout) {

        return RestTemplateFactory.getRestTemplate(CHECK_SERT, THROW_ON_ERROR, timeout);
    }

    public RestTemplate getRestTemplate(boolean throwOnError, int timeout) {

        //this.timeout = timeout;
        return  RestTemplateFactory.getRestTemplate(CHECK_SERT, throwOnError, timeout);
    }


    public RestTemplate getRestTemplate(boolean checkCert, boolean throwOnError) {

        return  RestTemplateFactory.getRestTemplate(checkCert, throwOnError, TIMEOUT);
    }

    public RestTemplate getRestTemplate(boolean checkCert, boolean throwOnError, int timeout) {

        return  RestTemplateFactory.getRestTemplate(checkCert, throwOnError, timeout);
    }*/

//
//    /**
//     * Perform get request
//     * @param url Url
//     * @return ResponseEntity<String>
//     */
//    public static ResponseEntity<String> get(RequestEntity<String> request, RestTemplate template) {
//
//        return template.exchange(request, String.class);
//    }
//
//    /**
//     * Perform get request
//     * @param url Url
//     * @return ResponseEntity<T>
//     */
//    public static <T,R> ResponseEntity<R> get(RequestEntity<T> request, RestTemplate template, Class<R> responseType) {
//
//        return template.exchange(request, responseType);
//    }


//    /**
//     * Perform get request
//     * @param url Url
//     * @return ResponseEntity<T>
//     */
//    public ResponseEntity<T> get(String url, Class<T> responseType) {
//
//        System.out.println(responseType.getSimpleName());
//
//        ResponseEntity<T> result;
//
//        //Set the headers you need send
//        final HttpHeaders headers = getHeaders();
//        //headers.set("User-Agent", userAgent);
//
//        //Create a new HttpEntity
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//
//        restTemplate.getFor
//
//        result = restTemplate.getForEntity(url, responseType);
//
//        return result;
//    }







//    /**
//     * Perform get request
//     * @param url Url
//     * @return ResponseEntity<byte[]>
//     */
//    public ResponseEntity<byte[]> download(String url, HttpHeaders headers, RestTemplate restTemplate) {
//
//        ResponseEntity<byte[]> result;
//
//        HttpHeaders headers = getHeaders();
//        //headers.set("User-Agent", userAgent);
//        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_OCTET_STREAM));
//
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//        result = restTemplate.exchange(url, HttpMethod.GET, entity, byte[].class);
//
//        return result;
//    }



//    /**
//     *
//     * @param url Url
//     * @param data String
//     * @return ResponseEntity<String>
//     */
//    public ResponseEntity<String> post(String url, String data, HttpHeaders headers, RestTemplate restTemplate) {
//
//        ResponseEntity<String> result;
//
//        //Set the headers you need send
//        HttpHeaders headers = getHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        //Create a new HttpEntity
//        HttpEntity<String> entity = new HttpEntity<>(data, headers);
//
//        result = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
//
//        return result;
//    }


//
//    /**
//     * Perform post request
//     * @param url Url
//     * @return ResponseEntity<T>
//     */
//    public ResponseEntity<T> post(String url, HttpHeaders headers, RestTemplate restTemplate, Class<T> responseType) {
//
//        ResponseEntity<T> result;
//
//        //Set the headers you need send
//        final HttpHeaders headers = getHeaders();
//
//        //Create a new HttpEntity
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        restTemplate.exchange()
//
//        result =  restTemplate.exchange(url, HttpMethod.POST, entity, responseType);
//        return result;
//    }


    // -------------------------------------------------------------------------


//    public static addCredentials(HttpHeaders headers) {
//
//        String auth = credentials.getUserPrincipal().getName() + ":" + credentials.getPassword();
//        String encodedAuth = Base64.encodeBase64String(auth.getBytes(Charset.forName("US-ASCII")));
//
//        String authHeader = "Basic " + encodedAuth;
//        result.set("Authorization", authHeader);
//
//
//
//    }

    /*

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
    */





    // -------------------------------------------------------------------------


//    public String getUserAgent() {
//        return userAgent;
//    }
//
//    public void setUserAgent(String userAgent) {
//        this.userAgent = userAgent;
//    }
}

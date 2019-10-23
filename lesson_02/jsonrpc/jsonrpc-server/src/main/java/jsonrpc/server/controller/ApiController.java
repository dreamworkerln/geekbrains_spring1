package jsonrpc.server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jsonrpc.protocol.dto.base.jrpc.JrpcRequestHeader;
import jsonrpc.protocol.dto.base.jrpc.JrpcResponse;
import jsonrpc.protocol.dto.base.http.HttpResponse;
import jsonrpc.protocol.dto.base.http.HttpResponseError;
import jsonrpc.protocol.dto.base.http.HttpResponseJRPC;
import jsonrpc.server.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.lang.invoke.MethodHandles;
import java.security.InvalidParameterException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import jsonrpc.server.handlers.base.ApiHandler;
import jsonrpc.server.handlers.base.MethodHandler;

@RestController
public class ApiController {

    private final ApplicationContext context;

    private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final static String ACCESS_TOKEN = "f229fbea-a4b9-40a8-b8ee-e2b47bc1391d";

    private static Map<String, MethodHandler> handlers = new ConcurrentHashMap<>();

    @Autowired
    public ApiController(ApplicationContext context) {
        this.context = context;

        loadHandlers();
    }

    @RequestMapping(value="/api", method = RequestMethod.POST)
    //public UserInfo register(@RequestBody UserProfile request)

    public ResponseEntity processRequest(@RequestBody String json) {

        // testing:

        // curl -v POST -H "Content-Type: application/json" -d '{"id":"22", "token":"f229fbea-a4b9-40a8-b8ee-e2b47bc1391d", "method": "userInfo", "params":{"email":"vasya@pupkin.ru", "nickname":"kfc"}}' "http://localhost:8085/api" ; echo ""

        // DEBUG
        // java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 -Dserver.port=8081 -jar chuck-api-1.0.jar


        Long id = null;

        // json-rpc response
        JrpcResponse jrpcResponse;

        // http response
        HttpResponse httpResponse;

        logger.info("POST '/api'");


        // now simply convert your JSON string into your UserProfile POJO
        // using Jackson's ObjectMapper.readValue() method, whose first
        // parameter your JSON parameter as String, and the second
        // parameter is the POJO class.
        //UserProfile JrpcRequestHeader = new ObjectMapper().readValue(profileJson, UserProfile.class);

        try {

            // Get jsonRpcHeader id, method name
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            // json-rpc request header (contains only method, token, id)
            JrpcRequestHeader jsonRpcHeader;
            jsonRpcHeader = objectMapper.readValue(json, JrpcRequestHeader.class);
            id = jsonRpcHeader.getId();

            final String method = jsonRpcHeader.getMethod();
            final String token = jsonRpcHeader.getToken();

            // ------------------------------------------------------------------------------
            // Parsing jrpc header

            if (id == null) {
                throw new IllegalArgumentException("JRPC no id specified");
            }
            if (Utils.isNullOrEmpty(token) || !token.equals(ACCESS_TOKEN)) {
                throw new IllegalAccessException("Forbidden");
            }
            if (Utils.isNullOrEmpty(method)) {
                throw new IllegalArgumentException("No JRPC method specified");
            }
            if (!handlers.containsKey(method)) {
                throw new IllegalArgumentException("JRPC method not found");
            }

            // ------------------------------------------------------------------------------
            // Get jrpc method params

            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

            JsonNode params = objectMapper.readTree(json).get("params");

            if (params == null) {
                throw new IllegalArgumentException("JRPC params==null");
            }

            // ------------------------------------------------------------------------------

            // executing RPC
            jrpcResponse = handlers.get(method).apply(params);

            // all going ok, prepare response
            httpResponse = new HttpResponseJRPC(jrpcResponse);
            httpResponse.setStatus(HttpStatus.OK);
        }
        catch (IllegalArgumentException | JsonProcessingException e) {

            String message = e.getMessage();
            if(Utils.isNullOrEmpty(message)) {
                message = HttpStatus.BAD_REQUEST.name();
            }
            httpResponse = new HttpResponseError(message, HttpStatus.BAD_REQUEST);
        }
        catch (IllegalAccessException e) {
            httpResponse = new HttpResponseError(e.getMessage(), HttpStatus.FORBIDDEN);
        }
        catch (Exception e) {
            httpResponse = new HttpResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // add request id
        httpResponse.setId(id);

        return new ResponseEntity<>(httpResponse, httpResponse.getStatus());
    }


//    private JrpcResponse parseRequest(String json) {
//
//
//
//
//
//    }




    /**
     * Fill requestHandlerList based on classes located in entities.message.request.*
     * <br>
     * Using reflection get all classes on server that handle clients requests
     */
    private void loadHandlers() {

        try {
            Map<String,Object> beans = context.getBeansWithAnnotation(ApiHandler.class);

            for (Map.Entry<String, Object> entry : beans.entrySet()) {

                ApiHandler annotation = context.findAnnotationOnBean(entry.getKey(), ApiHandler.class);

                // reference to handler method
                //noinspection unchecked
                MethodHandler methodHandler = (MethodHandler)entry.getValue();

                //RequestHandler_NOTUSED handler = new RequestHandler_NOTUSED(methodReference, annotation.request());

                handlers.put(annotation.method(), methodHandler);
            }
        } catch (BeansException e) {
            throw new RuntimeException(e);
        }

    }
}


/*
        try {

            Reflections ref = new Reflections("hello");

            for (Class<?> cl : ref.getTypesAnnotatedWith(ApiHandler.class)) {


                ApiHandler apiHandlerAnt = cl.getAnnotation(ApiHandler.class);
                Constructor<?> ctor = cl.getConstructor();

                //noinspection unchecked
                Function<JsonNode,ResponseBase> handler = (Function<JsonNode, ResponseBase>) ctor.newInstance();

                handlers.put(apiHandlerAnt.method(), handler);
            }

        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }*/

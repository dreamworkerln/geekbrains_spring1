package jsonrpc.resourceserver.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jsonrpc.protocol.http.HttpResponse;
import jsonrpc.protocol.jrpc.JrpcException;
import jsonrpc.protocol.jrpc.request.JrpcRequestHeader;
import jsonrpc.protocol.jrpc.response.JrpcErrorCode;
import jsonrpc.resourceserver.controller.jrpc.base.JrpcController;
import jsonrpc.resourceserver.controller.jrpc.base.JrpcMethod;
import jsonrpc.resourceserver.controller.jrpc.base.JrpcMethodHandler;
import jsonrpc.resourceserver.service.InvalidLogicException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.expression.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@RestController
public class ApiController {

    private final static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    // ToDo: move to Oauth2
    private final static String ACCESS_TOKEN = "f229fbea-a4b9-40a8-b8ee-e2b47bc1391d";
    private final static String API_VERSION = "1.0";
    private final static String API_PATH = "/api/"+API_VERSION+"/";

    private final ApplicationContext context;
    private final ObjectMapper objectMapper;



    // Map обработчиков jrpc запросов
    private final Map<String, JrpcMethodHandler> handlers = new ConcurrentHashMap<>();

    @Autowired
    public ApiController(ApplicationContext context, ObjectMapper objectMapper) {

        this.context = context;
        this.objectMapper = objectMapper;

        // scanning/loading handler beans
        loadHandlers();
    }


    @RequestMapping(value = API_PATH, method = RequestMethod.POST)
    public ResponseEntity processRequest(@RequestBody String request) {


        // Пилим аналог
        // https://spring.io/guides/tutorials/bookmarks/
        // Только json-rpc
        // https://www.jsonrpc.org/specification
        // соответственно http path один, а method указывается внутри json
        // => самодельный routing (аналог @GetMapping, будем маршрутизировать по классам/именам методов)


        // OAuth2 / token, etc -  сказали credentials передавать в инкапсулирующем потоке
        // (играющим роль транспортного для json-rpc)
        // в случае http - в заголовке,
        // в случае websocket - также в http заголовке (до апгрейда http в websocket)

        // REMOTE DEBUG
        // mvn -DskipTests package (will obtain .jar)
        //
        // java -agentlib:jdwp=transport=dt_socket,resourceserver=y,suspend=n,address=5005 -jar jsonrpc-resourceserver-0.1.jar
        // may set suspend=y to suspend application execution until debugger has been attached
        //
        // REMOTE JMX MONITORING AND DEBUG TCP v4
        // -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005
        // -Dcom.sun.management.jmxremote.port=5006
        // -Dcom.sun.management.jmxremote.rmi.port=5006
        // -Dcom.sun.management.jmxremote.local.only=true
        // -Dcom.sun.management.jmxremote.host=localhost
        // -Dcom.sun.management.jmxremote.authenticate=false
        // -Dcom.sun.management.jmxremote.ssl=false
        // -Dcom.sun.management.jmxremote.authenticate=false
        // -Djava.rmi.server.hostname=127.0.0.1
        //
        // remote-alias - алиас ssh-соединения, сконфигурировано в ~/.ssh/config
        //
        // Будет держать туннель(проброс портов) до посинения
        // ssh -fNT -L 8084:localhost:8084 -L 5005:localhost:5005 -L 9001:localhost:9001 remote-alias
        //
        //
        // Через 100с тунель автоматически закроется, если не был открыт сокет
        // (если соединение было установлено - будет ждать когда сокет закроется)
        // ssh -f -L 8084:localhost:8084 -L 5005:localhost:5005 -L 9001:localhost:9001 remote-alias sleep 100;
        //
        //
        // 8084 порт можно не пробрасывать (как и 9001)
        // по этим портам, можно например, ходить клиентом(браузер, curl) к приложению, если оно слушает этот порт
        //
        // -f run in background
        // -N no command execution
        // -T no terminal allocation


        Long id = null;

        // http response

        HttpResponse httpResponse;

        log.info("POST " + API_PATH + ": " + request);


        try {

            // JrpcRequestHeader не содержит свойство params, поэтому десереализация происходит без проблем
            // вытаскиваем все поля, кроме params
            JrpcRequestHeader jsonRpcHeader = objectMapper.readValue(request, JrpcRequestHeader.class);

            id = jsonRpcHeader.getId();
            String version = jsonRpcHeader.getVersion();
            final String method = jsonRpcHeader.getMethod();

            // Parsing jrpc header ------------------------------------------------------------------

            // Не поддерживаемая версия jrpc
            if (StringUtils.isBlank(version) || !version.equals("2.0")) {
                throw new JrpcException("JRPC version not supported", JrpcErrorCode.INVALID_REQUEST);
            }
            // Мы не поддерживаем уведомления для сервера
            if (id == null) {
                throw new JrpcException("JRPC has no id specified", JrpcErrorCode.INVALID_REQUEST);
            }
            // метод не указан
            if (StringUtils.isBlank(method)) {
                throw new JrpcException("No JRPC method specified",JrpcErrorCode.METHOD_NOT_FOUND);
            }
            // метод не найден
            if (!handlers.containsKey(method)) {
                throw new JrpcException("JRPC method not found", JrpcErrorCode.METHOD_NOT_FOUND);
            }

            // ------------------------------------------------------------------------------


            // reading params
            JsonNode params = objectMapper.readTree(request).get("params");

            // getting handler
            JrpcMethodHandler handler = handlers.get(method);


            JsonNode json;
            try {
                // executing RPC
                json = handler.apply(params);
            }
            // вышвыриваем наверх
            catch (InvocationTargetException e) {

                Throwable innerEx = e.getCause();
                Assert.notNull(innerEx, "InvocationTargetException is null");
                throw innerEx;
            }

            // encapsulate result to http
            httpResponse = HttpResponseFactory.getOk(json);

        }
        // Access denied
        catch (AccessDeniedException e) {
            log.error("Forbidden",e);
            // тут jrpc не инкапсулируется
            httpResponse = HttpResponseFactory.getForbidden();
        }
        // jrpc call error (method not found, etc)
        catch (JrpcException e) {
            log.error("JrpcError",e);
            httpResponse = HttpResponseFactory.getError(e);
        }

        // invalid request json
        catch (JsonProcessingException e) {
            log.error("Bad request json: " + e.getMessage(), e);
            httpResponse = HttpResponseFactory.getError(HttpStatus.BAD_REQUEST, e);
        }
        // invalid request params json(param parse error, wrong type, etc)
        catch (ParseException e) {
            log.error("Json param parse error: " + e.getMessage(), e);
            JrpcException ex = new JrpcException("Json param parse error", JrpcErrorCode.INVALID_PARAMS, e);
            httpResponse = HttpResponseFactory.getError(ex);
        }
        // params not passed validation
        catch (ConstraintViolationException e) {
            String message = "Param validation violation: " + e.getConstraintViolations().toString();
            log.error(message, e);
            JrpcException ex = new JrpcException(message, JrpcErrorCode.INVALID_PARAMS, e);
            httpResponse = HttpResponseFactory.getError(ex);

        }
        // hand-made param validation
        catch (ValidationException e) {
            log.error("Param validation violation: " + e.getMessage(), e);
            JrpcException ex = new JrpcException("Param validation violation", JrpcErrorCode.INVALID_PARAMS, e);
            httpResponse = HttpResponseFactory.getError(ex);
        }
        // params logic violation
        catch (InvalidLogicException e) {
            log.error("Logic violation: " + e.getMessage(), e);
            JrpcException ex = new JrpcException("Logic violation", JrpcErrorCode.INVALID_PARAMS, e);
            httpResponse = HttpResponseFactory.getError(ex);
        }

//        // ????
//        catch (IllegalArgumentException e) {
//            log.error("Illegal argument ???: " + e.getMessage(), e);
//            throw new RuntimeException("Illegal argument", e);
////            JrpcException ex = new JrpcException("Illegal argument", JrpcErrorCode.INVALID_PARAMS, e);
////            httpResponse = HttpResponseFactory.getError(ex);
//        }
        catch (Throwable e) {
            log.error("Internal resourceserver error in controller: " + e.getMessage(), e);
            httpResponse = HttpResponseFactory.getError(HttpStatus.INTERNAL_SERVER_ERROR, e);
        }

        // add request id to response (if have one)
        if (httpResponse.getResult()!= null) {
            httpResponse.getResult().setId(id);
        }

        return new ResponseEntity<>(httpResponse.getResult(), httpResponse.getStatus());
    }



    // =================================================================================================

    /**
     * Fill handlers based on beans annotated with @JrpcController
     * <br>
     * Using reflection to get all classes and theirs methods that handle clients requests
     */
    private void loadHandlers() {

        try {

            // Ask spring to find (and load if not loaded?) all beans annotated with @JrpcController
            Map<String,Object> beans = context.getBeansWithAnnotation(JrpcController.class);
            //beans.keySet().forEach(System.out::println);

            // https://stackoverflow.com/questions/27929965/find-method-level-custom-annotation-in-a-spring-context
            for (Map.Entry<String, Object> entry : beans.entrySet()) {

                Object bean = entry.getValue();
                Class<?> beanClass = bean.getClass();
                JrpcController jrpcController = beanClass.getAnnotation(JrpcController.class);

                // bean is an AOP proxy
                if (jrpcController == null) {
                    beanClass = AopProxyUtils.ultimateTargetClass(bean);
                    jrpcController = beanClass.getAnnotation(JrpcController.class);

                }
                
                // Ищем в бине метод, помеченный аннотацией @JrpcMethod
                for (Method method : beanClass.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(JrpcMethod.class)) {

                        //Should give you expected results
                        JrpcMethod jrpcMethod = method.getAnnotation(JrpcMethod.class);

                        // Checked(как и unchecked) исключения будут проброшены к вызывающему
                        JrpcMethodHandler handler = params -> (JsonNode)method.invoke(bean,params);

                        handlers.put(jrpcController.path() + "." + jrpcMethod.method(), handler);
                    }
                }
            }

        } catch (BeansException e) {
            throw new RuntimeException(e);
        }

    }


    // ===============================================================================================


    //@Scheduled(fixedRate = 5000)
    //@Scheduled(cron = "*/3 * * * * *")
    public void scheduleTaskUsingCronExpression() throws InterruptedException {

        long now = Instant.now().getEpochSecond();
        System.out.println(
                "ОЧКО - schedule tasks using cron jobs - " + now);

        TimeUnit.DAYS.sleep(100000);
    }

    //@Scheduled(fixedRate = 6000)
    //@Scheduled(cron = "*/4 * * * * *")
    public void scheduleTaskUsingCronExpression2() throws InterruptedException {

        long now = Instant.now().getEpochSecond();
        System.out.println(
                "ЗАЛУПА - schedule tasks using cron jobs - " + now);

        TimeUnit.DAYS.sleep(100000);

    }

}






/*
          ____________________________________________________________________________________________
          ///////////////////////////********* СКЛАДОПОМОЙКА КОДА *********\\\\\\\\\\\\\\\\\\\\\\\\\\\
          =============================================================================================





//
//    private AbstractDto foo(JsonNode node) {
//        System.out.println("Ololo!!!");
//
//        return null;
//    }


//            // кривые параемты, запрос нарушает бизнес-логику
//            if (ex instanceof JrpcException) {
//                httpResponse = HttpResponseFactory.getError((JrpcException)ex);
//            }
//            // ...
//            else {
//                log.error("Internal resourceserver error: " + e.getMessage(), e);
//                httpResponse = HttpResponseFactory.getError(HttpStatus.INTERNAL_SERVER_ERROR, e);
//            }


/*
        try {

            Reflections ref = new Reflections("hello");

            for (Class<?> cl : ref.getTypesAnnotatedWith(JrpcController.class)) {


                JrpcController apiHandlerAnt = cl.getAnnotation(JrpcController.class);
                Constructor<?> ctor = cl.getConstructor();

                //noinspection unchecked
                Function<JsonNode,ResponseBase> handler = (Function<JsonNode, ResponseBase>) ctor.newInstance();

                handlers.save(apiHandlerAnt.method(), handler);
            }

        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }*/



//objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

//objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


    /*
    @RequestMapping(value = "/**", method = RequestMethod.POST)
    public void processRequest(HttpServletRequest request,
                                         HttpServletResponse response) {

        System.out.println(request);

        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
    */



//            try {
//                params = objectMapper.readTree(request).get("params");
//            } catch (JsonProcessingException e) {
//                throw new JrpcException("Invalid params", JrpcErrorCode.INVALID_REQUEST, e);
//            }

// ------------------------------------------------------------------------------




//         Фокус - внутри httpResponse(HttpResponseJRPC)
//         есть поле JsonNode result;
//         Так ObjectMapper, который серализует ResponseEntity<> в json
//         при возврате результата ResponseEntity<>
//         (если написать ResponseEntity<String> то не будет сериализовать, вернет String)
//         для поля JsonNode вызовет просто JsonNode.toPrettyString()
//
//         Т.е. поле JsonNode result играет роль типа var(C#)
//         И туда можно толкать как одиночные объекты, так и их коллекции, массивы
//         без оборачивания в классы-обертки, так как все возвращается все равно в
//         объекте JsonNode





//
//    // вытаскиваем cause() исключение и бросам его заново (exception flatMap)
//    handleInvocationException(e);
//// кривые параемты, запрос нарушает бизнес-логику
//                if (ex instanceof JrpcException) {
//                        httpResponse = HttpResponseFactory.getError((JrpcException)ex);
//                        }
//                        // ...
//                        else {
//                        log.error("Internal resourceserver error: " + e.getMessage(), e);
//                        httpResponse = HttpResponseFactory.getError(HttpStatus.INTERNAL_SERVER_ERROR, e);
//                        }
//
//




//    private void handleInvocationException(InvocationTargetException e) {
//
//        Throwable innerEx = e.getCause();
//        Assert.notNull(innerEx, "InvocationTargetException is null");
//
//        if (innerEx instanceof ConstraintViolationException) {
//
//            String message = "Param validation failed: " +
//                             ((ConstraintViolationException) innerEx)
//                                     .getConstraintViolations()
//                                     .toString();
//
//            log.error(message, innerEx);
//            throw new IllegalArgumentException(message,  innerEx);
//        }
//
//
//
////        // should be not null
////        Throwable innerEx = e.getCause();
////        Assert.notNull(innerEx, "InvocationTargetException is null");
////
////        if (innerEx instanceof ConstraintViolationException) {
////
////            String message = "Param validation failed: " +
////                             ((ConstraintViolationException) innerEx)
////                                     .getConstraintViolations()
////                                     .toString();
////
////            log.error(message, innerEx);
////            return new JrpcException(message, JrpcErrorCode.INVALID_PARAMS, innerEx);
////        }
////
////        if (innerEx instanceof IllegalArgumentException) {
////            log.error("Param validation failed: ", innerEx);
////            return new JrpcException("Param validation failed", JrpcErrorCode.INVALID_PARAMS, innerEx);
////        }
////
////        return e;
//    }
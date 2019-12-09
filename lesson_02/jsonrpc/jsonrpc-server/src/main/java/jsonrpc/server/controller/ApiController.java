package jsonrpc.server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jsonrpc.protocol.dto.base.jrpc.JrpcRequestHeader;
import jsonrpc.protocol.dto.base.http.HttpResponse;
import jsonrpc.protocol.dto.base.http.HttpResponseError;
import jsonrpc.protocol.dto.base.http.HttpResponseJRPC;
import jsonrpc.server.controller.jrpc.base.JrpcController;
import jsonrpc.server.controller.jrpc.base.JrpcMethod;
import jsonrpc.server.controller.jrpc.base.JrpcMethodHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@RestController
public class ApiController {

    private final ApplicationContext context;

    private final ObjectMapper objectMapper;

    private final static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    // Next move to Oauth2
    private final static String ACCESS_TOKEN = "f229fbea-a4b9-40a8-b8ee-e2b47bc1391d";

    // Map обработчиков jrpc запросов
    private final Map<String, JrpcMethodHandler> handlers = new ConcurrentHashMap<>();

    @Autowired
    public ApiController(ApplicationContext context, ObjectMapper objectMapper) {
        this.context = context;
        this.objectMapper = objectMapper;

        // scanning/loading handlers
        loadHandlers();
    }

    /*
    @RequestMapping(value = "/**", method = RequestMethod.POST)
    public void processRequest(HttpServletRequest request,
                                         HttpServletResponse response) {

        System.out.println(request);

        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
    */




    @RequestMapping(value = "/api", method = RequestMethod.POST)
    //public UserDto register(@RequestBody UserByNickAndMail request)

    public ResponseEntity processRequest(@RequestHeader("token") String token, @RequestBody String json) {

        // OAuth2 / tokens, etc -  сказали credentials передавать в инкапсулирующем потоке
        // (играющим роль транспортного для json-rpc)
        // в случае http - в заголовке,
        // в случае websocket - также в http заголовке (до апгрейда http в websocket)

        // REMOTE DEBUG
        // mvn -DskipTests package (will obtain .jar)
        //
        // java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 -jar jsonrpc-server-0.1.jar
        // may set suspend=y to suspend application execution until debugger has been attached
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
        //
        // -f run in background
        // -N no command execution
        // -T no terminal allocation


        Long id = null;

        // jrpc response
        JsonNode jrpcResult;

        // http response
        HttpResponse httpResponse;

        log.info("POST '/api': " + json);


        // Пилим аналог
        // https://spring.io/guides/tutorials/bookmarks/
        // Только json-rpc
        // соответственно http path один, а method указывается внутри json
        // => самодельный routing (аналог @GetMapping, будем маршрутизировать по именам методов)

        try {

            //objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            // JrpcRequestHeader не содержит свойство params, поэтому десереализация происходит без проблем
            JrpcRequestHeader jsonRpcHeader = objectMapper.readValue(json, JrpcRequestHeader.class);



            id = jsonRpcHeader.getId();
            final String method = jsonRpcHeader.getMethod();
            //final String token = jsonRpcHeader.getToken();

            // ------------------------------------------------------------------------------
            // Parsing jrpc header

            if (id == null) {
                throw new IllegalArgumentException("JRPC no id specified");
            }



            if (StringUtils.isBlank(token) || !token.equals(ACCESS_TOKEN)) {
                throw new IllegalAccessException("Forbidden");
            }
            if (StringUtils.isBlank(method)) {
                throw new IllegalArgumentException("No JRPC method specified");
            }
            if (!handlers.containsKey(method)) {
                throw new IllegalArgumentException("JRPC method not found");
            }

            // ------------------------------------------------------------------------------
            //objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

            // reading params
            JsonNode params = objectMapper.readTree(json).get("params");

            // ------------------------------------------------------------------------------


            JrpcMethodHandler handler = handlers.get(method);

            // executing RPC
            jrpcResult = handler.apply(params);

            // all going ok, prepare response
            httpResponse = new HttpResponseJRPC(jrpcResult);
            httpResponse.setStatus(HttpStatus.OK);

        }
        catch (IllegalArgumentException | JsonProcessingException e) {

            log.error("",e);

            String message = e.getMessage();
            if(StringUtils.isBlank(message)) {
                message = HttpStatus.BAD_REQUEST.name();
            }
            httpResponse = new HttpResponseError(message, HttpStatus.BAD_REQUEST);
        }
        catch (IllegalAccessException e) {
            log.error("",e);
            httpResponse = new HttpResponseError(e.getMessage(), HttpStatus.FORBIDDEN);
        }
        catch (Exception e) {
            log.error("",e);
            httpResponse = new HttpResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // add request id (if have one)
        httpResponse.setId(id);

        // Фокус - внутри httpResponse(HttpResponseJRPC)
        // есть поле JsonNode result;
        // Так ObjectMapper, который серализует ResponseEntity<> в json
        // при возврате результата ResponseEntity<>
        // (если написать ResponseEntity<String> то не будет сериализовать, вернет String)
        // для поля JsonNode вызовет просто JsonNode.toPrettyString()
        //
        // Т.е. поле JsonNode result играет роль типа var(C#)
        // И туда можно толкать как одиночные объекты, так и их коллекции, массивы
        // без оборачивания в классы-обертки, так как все возвращается все равно в
        // объекте JsonNode

        return new ResponseEntity<>(httpResponse, httpResponse.getStatus());
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

                // Ищем в бине метод, помеченный аннотацией @JrpcMethod
                for (Method method : beanClass.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(JrpcMethod.class)) {

                        //Should give you expected results
                        JrpcMethod jrpcMethod = method.getAnnotation(JrpcMethod.class);


                        JrpcMethodHandler handler = jsonNode -> {
                            try {
                                return (JsonNode) method.invoke(bean, jsonNode);
                            } catch (IllegalAccessException | InvocationTargetException e) {

                                // выбрасываем наверх причину InvocationTargetException -
                                // Что-то там упало в обработчике метода jrpc upcast into unchecked exception



                                // ToDo: этот кусок кода надо улучшать
                                // https://www.baeldung.com/java-lambda-exceptions
                                // https://github.com/pivovarit/throwing-function


                                Throwable cause = e.getCause();

                                // если это некорректный данные в запросе пользователя
                                if (cause instanceof IllegalArgumentException) {

                                    // Чем вызвано это исключение ?
                                    Throwable innerEx = cause.getCause();
                                    if (innerEx instanceof ConstraintViolationException) {

                                        // залогируем
                                        log.error(((ConstraintViolationException)innerEx)
                                                .getConstraintViolations()
                                                .toString(), innerEx);
                                    }


                                    // вернем 400
                                    throw new IllegalArgumentException(cause.getMessage(), e);
                                }

                                // Если это исключение из репозитория spring
                                if (cause instanceof InvalidDataAccessApiUsageException) {

                                    // Чем вызвано исключение в репозитории spring?
                                    Throwable dataEx = cause.getCause();

                                    // Надо завести свой тип для исключения -
                                    // "Недозволенное действие пользоваетля" вместо IllegalArgumentException

                                    // если оно вызвано некорректным действием пользователя
                                    if (dataEx instanceof IllegalArgumentException) {
                                        // вернем 400
                                        throw new IllegalArgumentException(dataEx.getMessage(), e);
                                    }

                                    // Это исключения репозитория spring,
                                    // но хз что это конктретно - мож это не мы, оно само
                                    throw (RuntimeException) cause;
                                }

                                // Внутреннего исключения нет
                                // (или там не IllegalArgumentException и не InvalidDataAccessApiUsageException)
                                throw new RuntimeException(e);
                            }

                        };

                        handlers.put(jrpcController.path() + "." + jrpcMethod.method(), handler);
                    }
                }

            }

        } catch (BeansException e) {
            throw new RuntimeException(e);
        }

    }


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


//
//    private AbstractDto foo(JsonNode node) {
//        System.out.println("Ololo!!!");
//
//        return null;
//    }






}


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

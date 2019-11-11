package jsonrpc.server.handlers.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;


public abstract class MethodHandlerBase /*JrpcMethodHandler*/ {

    protected ObjectMapper objectMapper;
    //protected final ModelMapper modelMapper;

    //protected Mapper mapper;


    @Autowired
    private final void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

//    public MethodHandlerBase() {
//        requestType = getRequestType();
//    }

    /*
    @Override
    public JrpcResult apply(JsonNode params) {

        // read https://www.baeldung.com/jackson-inheritance
        // and upgrade code

        ObjectMapper objectMapper = new ObjectMapper();
        // parse request
        JrpcParameter request;
        try {
            request = objectMapper.treeToValue(params, requestType);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }

        return handle(request);
    }
    */

    /*
    //@Override
    public void accept(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    */

    /*
    protected abstract JrpcResult handle(JrpcParameter jrpcRequest);
    
    protected abstract Class<? extends JrpcParameter> getRequestType();
    */

    // Entity to DTO
//    protected abstract JrpcParameter convertToDto(Request request);
}

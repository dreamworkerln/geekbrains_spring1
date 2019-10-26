package jsonrpc.server.handlers.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;


public abstract class MethodHandlerBase /*JrpcMethodHandler*/ {

    //private Class<? extends JrpcParameter> requestType;

    protected final ObjectMapper objectMapper;
    protected final ModelMapper modelMapper;

    @Autowired
    protected MethodHandlerBase(ObjectMapper objectMapper, ModelMapper modelMapper) {
        this.objectMapper = objectMapper;
        this.modelMapper = modelMapper;

        setMappings();
    }

    protected abstract void setMappings();

//    public MethodHandlerBase() {
//        requestType = getRequestType();
//    }

    /*
    @Override
    public JrpcResponse apply(JsonNode params) {

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
    protected abstract JrpcResponse handle(JrpcParameter jrpcRequest);
    
    protected abstract Class<? extends JrpcParameter> getRequestType();
    */

    // Entity to DTO
//    protected abstract JrpcParameter convertToDto(Request request);
}

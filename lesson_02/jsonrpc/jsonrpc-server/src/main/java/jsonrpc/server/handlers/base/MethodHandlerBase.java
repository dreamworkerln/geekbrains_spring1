package jsonrpc.server.handlers.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jsonrpc.protocol.dto.base.jrpc.JrpcRequest;
import jsonrpc.protocol.dto.base.jrpc.JrpcResponse;
import jsonrpc.server.entities.base.Request;



public abstract class MethodHandlerBase /*JrpcMethodHandler*/ {

    //private Class<? extends JrpcRequest> requestType;

    protected ObjectMapper objectMapper = new ObjectMapper();

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
        JrpcRequest request;
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
    protected abstract JrpcResponse handle(JrpcRequest jrpcRequest);
    
    protected abstract Class<? extends JrpcRequest> getRequestType();
    */

    // Entity to DTO
//    protected abstract JrpcRequest convertToDto(Request request);
}

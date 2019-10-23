package jsonrpc.server.handlers.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jsonrpc.protocol.dto.base.jrpc.JrpcParam;
import jsonrpc.protocol.dto.base.jrpc.JrpcResponse;

public abstract class MethodHandlerBase implements MethodHandler {

    private Class<? extends JrpcParam> requestType;

    public MethodHandlerBase() {
        requestType = getRequestType();
    }

    @Override
    public JrpcResponse apply(JsonNode params) {

        // read https://www.baeldung.com/jackson-inheritance
        // and upgrade code

        ObjectMapper objectMapper = new ObjectMapper();
        // parse request
        JrpcParam request;
        try {
            request = objectMapper.treeToValue(params, requestType);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }

        return handle(request);
    }


    protected abstract JrpcResponse handle(JrpcParam jrpcParam);
    
    protected abstract Class<? extends JrpcParam> getRequestType();
}

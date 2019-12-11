package jsonrpc.protocol.jrpc.request;

import com.fasterxml.jackson.databind.JsonNode;


public class JrpcRequest extends JrpcRequestHeader {

    private JsonNode params;

    public JsonNode getParams() {
        return params;
    }

    public void setParams(JsonNode param) {
        this.params = param;
    }
}

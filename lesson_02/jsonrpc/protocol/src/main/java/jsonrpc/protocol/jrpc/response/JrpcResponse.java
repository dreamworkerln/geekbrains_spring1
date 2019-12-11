package jsonrpc.protocol.jrpc.response;

import com.fasterxml.jackson.databind.JsonNode;
import jsonrpc.protocol.jrpc.JrpcBase;
import lombok.Data;

@Data
public class JrpcResponse extends JrpcBase {

    private JsonNode result;

    public JrpcResponse(JsonNode result) {
        this.result = result;
    }
}

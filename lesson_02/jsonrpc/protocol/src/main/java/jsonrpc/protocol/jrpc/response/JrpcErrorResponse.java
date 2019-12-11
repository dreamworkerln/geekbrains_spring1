package jsonrpc.protocol.jrpc.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import jsonrpc.protocol.jrpc.JrpcBase;
import lombok.Data;

/**
 * Represent json-rpc execution error response
 */
@Data
public class JrpcErrorResponse extends JrpcBase {


    @JsonProperty("error")
    ErrorBody body = new ErrorBody();

    private JrpcErrorResponse(){}

    public JrpcErrorResponse(String message, JrpcErrorCode code) {
        this.body.message = message;
        this.body.code = code;
    }

    public JrpcErrorResponse(String message, JrpcErrorCode code, JsonNode data) {
        this.body.message = message;
        this.body.code = code;
        this.body.data = data;
    }

    @Data
    private static class ErrorBody {
        // сообщение ошибки
        private String message;
        // код ошибки
        private JrpcErrorCode code;
        // additional error data
        private JsonNode data;
    }
}

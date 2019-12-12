package jsonrpc.protocol.jrpc;

import com.fasterxml.jackson.databind.JsonNode;
import jsonrpc.protocol.jrpc.response.JrpcErrorCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class JrpcException extends RuntimeException {

    private JrpcErrorCode code; // код jrpc ошибки

    private JsonNode data;

    private JrpcException(){}

    public JrpcException(JrpcErrorCode code) {
        this.code = code;
    }

    public JrpcException(String s, JrpcErrorCode code) {
        super(s);
        this.code = code;
    }

    public JrpcException(String s, JrpcErrorCode code, Throwable throwable) {
        super(s, throwable);
        this.code = code;
    }

    public JrpcException(JrpcErrorCode code, Throwable throwable) {
        super(throwable);
        this.code = code;
    }

}

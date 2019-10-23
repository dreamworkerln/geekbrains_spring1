package jsonrpc.protocol.dto.base.jrpc;

import org.springframework.http.HttpStatus;

/**
 * Represent json-rpc execution error response
 */
public class JrpcError {

    protected String message;

    protected int code;

    public JrpcError() {
    }

    public JrpcError(String message, HttpStatus status) {
        this.message = message;
        this.code = status.value();
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}

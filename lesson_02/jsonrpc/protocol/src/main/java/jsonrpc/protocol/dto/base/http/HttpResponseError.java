package jsonrpc.protocol.dto.base.http;

import jsonrpc.protocol.dto.base.jrpc.JrpcError;
import org.springframework.http.HttpStatus;

public class HttpResponseError extends HttpResponse {

    protected JrpcError error;

    public HttpResponseError() {}

    public HttpResponseError(String message, HttpStatus status) {
        this.setStatus(status);
        this.error = new JrpcError(message, status);
    }

    public JrpcError getError() {
        return error;
    }
}

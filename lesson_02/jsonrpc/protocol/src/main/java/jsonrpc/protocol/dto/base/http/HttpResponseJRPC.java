package jsonrpc.protocol.dto.base.http;

import jsonrpc.protocol.dto.base.jrpc.JrpcResponse;

/**
 * HTTP response that encapsulate jrpc
 */
public class HttpResponseJRPC extends HttpResponse {

    protected JrpcResponse result;

    public HttpResponseJRPC() {}

    public HttpResponseJRPC(JrpcResponse result) {
        this.result = result;
    }

    public JrpcResponse getResult() {
        return result;
    }

    public void setResult(JrpcResponse result) {
        this.result = result;
    }
}
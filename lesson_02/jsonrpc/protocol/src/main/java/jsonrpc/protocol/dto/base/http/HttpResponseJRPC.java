package jsonrpc.protocol.dto.base.http;

import jsonrpc.protocol.dto.base.jrpc.JrpcResult;

/**
 * HTTP response that encapsulate jrpc
 */
public class HttpResponseJRPC extends HttpResponse {

    protected JrpcResult result;

    public HttpResponseJRPC() {}

    public HttpResponseJRPC(JrpcResult result) {
        this.result = result;
    }

    public JrpcResult getResult() {
        return result;
    }

    public void setResult(JrpcResult result) {
        this.result = result;
    }
}
package jsonrpc.protocol.dto.base.http;


import jsonrpc.protocol.dto.base.jrpc.AbstractDto;

/**
 * HTTP response that encapsulate jrpc
 */
public class HttpResponseJRPC extends HttpResponse {

    protected AbstractDto result;

    public HttpResponseJRPC() {}

    public HttpResponseJRPC(AbstractDto result) {
        this.result = result;
    }

    public AbstractDto getResult() {
        return result;
    }

    public void setResult(AbstractDto result) {
        this.result = result;
    }
}
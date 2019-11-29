package jsonrpc.protocol.dto.base.http;


import com.fasterxml.jackson.databind.JsonNode;
import jsonrpc.protocol.dto.base.jrpc.AbstractDto;

/**
 * HTTP response that encapsulate jrpc
 */
public class HttpResponseJRPC extends HttpResponse {

    protected JsonNode result;

    public HttpResponseJRPC() {}

    public HttpResponseJRPC(JsonNode result) {
        this.result = result;
    }

    public JsonNode getResult() {
        return result;
    }

    public void setResult(JsonNode result) {
        this.result = result;
    }
}
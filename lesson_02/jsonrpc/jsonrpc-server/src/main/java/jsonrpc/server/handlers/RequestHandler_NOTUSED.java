package jsonrpc.server.handlers;

import jsonrpc.protocol.dto.base.jrpc.JrpcParam;
import jsonrpc.server.handlers.base.MethodHandler;

public class RequestHandler_NOTUSED {


    private Class<? extends JrpcParam> requestClass;
    private MethodHandler method;

    public RequestHandler_NOTUSED(MethodHandler method, Class<? extends JrpcParam> requestClass) {
        this.requestClass = requestClass;
        this.method = method;
    }

    public MethodHandler getMethod() {
        return method;
    }

    public Class<? extends JrpcParam> getRequest() {
        return requestClass;
    }


}

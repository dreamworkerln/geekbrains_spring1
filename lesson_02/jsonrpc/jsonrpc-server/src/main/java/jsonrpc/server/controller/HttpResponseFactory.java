package jsonrpc.server.controller;

import com.fasterxml.jackson.databind.JsonNode;
import jsonrpc.protocol.http.HttpResponse;
import jsonrpc.protocol.jrpc.JrpcBase;
import jsonrpc.protocol.jrpc.JrpcException;
import jsonrpc.protocol.jrpc.response.JrpcErrorCode;
import jsonrpc.protocol.jrpc.response.JrpcErrorResponse;
import jsonrpc.protocol.jrpc.response.JrpcResponse;
import org.springframework.http.HttpStatus;


// Создаватель HTTP ответов, инкапсулирует jrpc внутрь http
class HttpResponseFactory {

    static HttpResponse getOk(JrpcBase jrpcResponse) {
        return new HttpResponse(HttpStatus.OK, jrpcResponse);
    }

    static HttpResponse getOk(JsonNode json) {
        return new HttpResponse(HttpStatus.OK, new JrpcResponse(json));
    }


    // no jrpc encapsulation
    static HttpResponse getForbidden() {
        return new HttpResponse(HttpStatus.FORBIDDEN);
    }


    static HttpResponse getError(JrpcException e) {

        // Include inner exception message
        String message = e.getMessage();
        if (e.getCause()!= null) {
            message = message + ": " + e.getCause().getMessage();
        }
        JrpcErrorResponse jrpcResult = new JrpcErrorResponse(message, e.getCode(), e.getData());
        return new HttpResponse(HttpStatus.BAD_REQUEST, jrpcResult);
    }


    static HttpResponse getError(HttpStatus status, Throwable e) {

        String message = "Invalid request json: " + e.getMessage();
        JrpcErrorResponse jrpcResult = new JrpcErrorResponse(message, JrpcErrorCode.INVALID_REQUEST);
        return new HttpResponse(status, jrpcResult);
    }
}

package jsonrpc.protocol.jrpc.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jsonrpc.protocol.jrpc.response.jsonserialize.JrpcErrorCodeSerializer;

@JsonSerialize(using = JrpcErrorCodeSerializer.class)
public enum JrpcErrorCode {

    PARSE_ERROR(-32700),
    INVALID_REQUEST(-32600),
    METHOD_NOT_FOUND(-32601),
    INVALID_PARAMS(-32602),
    INTERNAL_SERVER_ERROR(-32603),
    //
    // handmade
    UNAUTHORIZED(-32609),
    FORBIDDEN(-32610);


    private int code;

    JrpcErrorCode(int code) {
        this.code = code;
    }

    public int value() {
        return code;
    }
}
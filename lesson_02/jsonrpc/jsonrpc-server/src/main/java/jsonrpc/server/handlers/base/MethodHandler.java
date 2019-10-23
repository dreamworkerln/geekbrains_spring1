package jsonrpc.server.handlers.base;

import com.fasterxml.jackson.databind.JsonNode;
import jsonrpc.protocol.dto.base.jrpc.JrpcResponse;

import java.util.function.Function;

public interface MethodHandler extends Function<JsonNode,JrpcResponse> {}

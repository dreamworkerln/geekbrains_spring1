package jsonrpc.server.handlers.base;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jsonrpc.protocol.dto.base.jrpc.JrpcResponse;

import java.util.function.Consumer;
import java.util.function.Function;

public interface JrpcMethodHandler extends Function<JsonNode,JrpcResponse>/*, Consumer<ObjectMapper> */{}

package jsonrpc.resourceserver.controller.jrpc.base;

import com.fasterxml.jackson.databind.JsonNode;
import com.pivovarit.function.ThrowingFunction;

/**
 * Функциональный интерфейс обработчика jrpc запроса
 * alias
 */
public interface JrpcMethodHandler extends ThrowingFunction<JsonNode,JsonNode, Exception> {}
package jsonrpc.server.handlers.base;

import com.fasterxml.jackson.databind.JsonNode;
import jsonrpc.protocol.dto.base.jrpc.AbstractDto;

import java.util.function.Function;

/**
 * Функциональный интерфейс обработчика jrpc запроса
 * (Просто более короткий alias для Function<JsonNode,AbstractDto>)
 */
public interface JrpcMethodHandler extends Function<JsonNode,AbstractDto> {}

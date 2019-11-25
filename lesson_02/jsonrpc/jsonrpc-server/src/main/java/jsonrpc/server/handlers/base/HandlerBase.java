package jsonrpc.server.handlers.base;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jsonrpc.protocol.dto.base.param.GetByIdDto;
import jsonrpc.server.entities.base.param.GetById;
import jsonrpc.server.entities.base.param.GetByIdMapper;

public abstract class HandlerBase {

    protected final ObjectMapper objectMapper;
    private final GetByIdMapper getByIdMapper;

    protected HandlerBase(ObjectMapper objectMapper, GetByIdMapper getByIdMapper) {
        this.objectMapper = objectMapper;
        this.getByIdMapper = getByIdMapper;
    }


    protected GetById getByIdRequest(JsonNode params) {

        // parsing request
        GetById result;
        try {
            GetByIdDto requestDto = objectMapper.treeToValue(params, GetByIdDto.class);
            result = getByIdMapper.toEntity(requestDto);

            // validate request
            GetById.validate(result);

            //request = objectMapper.treeToValue(params, GetById.class);
        }
        // All parse/deserialize errors interpret as 400 error
        catch (Exception e) {
            //log.error("json parse error: " + params.toPrettyString(), e);
            throw new IllegalArgumentException("Jackson parse error", e);
        }

        return result;
    }

}

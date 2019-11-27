package jsonrpc.server.handlers.base;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jsonrpc.protocol.dto.base.param.GetByIdParamDto;
import jsonrpc.protocol.dto.base.param.GetByListIdParamDto;
import jsonrpc.server.entities.base.param.GetByIdParam;
import jsonrpc.server.entities.base.param.GetByIdMapper;
import jsonrpc.server.entities.base.param.GetByListIdMapper;
import jsonrpc.server.entities.base.param.GetByListIdParam;

public abstract class HandlerBase {

    protected final ObjectMapper objectMapper;
    private final GetByIdMapper getByIdMapper;
    private final GetByListIdMapper getByListIdMapper;


    protected HandlerBase(ObjectMapper objectMapper,
                          GetByIdMapper getByIdMapper,
                          GetByListIdMapper getByListIdMapper) {
        this.objectMapper = objectMapper;
        this.getByIdMapper = getByIdMapper;
        this.getByListIdMapper = getByListIdMapper;
    }


    protected GetByIdParam getByIdRequest(JsonNode params) {

        // parsing request
        GetByIdParam result;
        try {
            GetByIdParamDto requestDto = objectMapper.treeToValue(params, GetByIdParamDto.class);
            result = getByIdMapper.toEntity(requestDto);

            // validate request
            GetByIdParam.validate(result);

            //request = objectMapper.treeToValue(params, GetById.class);
        }
        // All parse/deserialize errors interpret as 400 error
        catch (Exception e) {
            //log.error("json parse error: " + params.toPrettyString(), e);
            throw new IllegalArgumentException("Jackson parse error", e);
        }

        return result;
    }


    protected GetByListIdParam getByListIdRequest(JsonNode params) {

        // parsing request
        GetByListIdParam result;
        try {
            GetByListIdParamDto requestDto = objectMapper.treeToValue(params, GetByListIdParamDto.class);
            result = getByListIdMapper.toEntity(requestDto);

            // validate request
            GetByListIdParam.validate(result);
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Jackson parse error", e);
        }

        return result;
    }

}

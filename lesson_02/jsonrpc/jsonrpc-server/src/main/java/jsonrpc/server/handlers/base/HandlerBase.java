package jsonrpc.server.handlers.base;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jsonrpc.protocol.dto.base.param.IdDto;
import jsonrpc.protocol.dto.base.param.ListIdDto;

import java.util.List;

public abstract class HandlerBase {

    protected final ObjectMapper objectMapper;


    protected HandlerBase(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    protected Long getId(JsonNode params) {

        Long result;

        // parsing request
        try {
            IdDto idDto = objectMapper.treeToValue(params, IdDto.class);
            // validate ... end
            result = idDto.getId();
        }
        // All parse/deserialize errors interpreted as 400 error - do not remove this try/catch
        catch (Exception e) {
            throw new IllegalArgumentException("Jackson parse error", e);
        }

        return result;
    }


    protected List<Long> getIdList(JsonNode params) {

        List<Long> result;
        try {

            ListIdDto listIdDto = objectMapper.treeToValue(params, ListIdDto.class);
            ListIdDto.validate(listIdDto);
            result = listIdDto.getList();
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Jackson parse error", e);
        }

        return result;
    }

}

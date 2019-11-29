package jsonrpc.server.handlers.base;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jsonrpc.protocol.dto.product.ProductDto;

import java.util.Arrays;
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
            result = objectMapper.treeToValue(params, Long.class);

            // validate
            if (result == null || result < 0) {
                throw new IllegalArgumentException("Iid == null < 0");
            }
        }
        // All parse/deserialize errors interpreted as 400 error - do not remove this try/catch
        catch (Exception e) {
            throw new IllegalArgumentException("Jackson param parse error", e);
        }
        return result;
    }


    protected List<Long> getIdList(JsonNode params) {

        List<Long> result;
        try {

            if (params == null) {
                throw new IllegalArgumentException("ListId == null");
            }


            // https://stackoverflow.com/questions/6349421/how-to-use-jackson-to-deserialise-an-array-of-objects
            result = Arrays.asList(objectMapper.treeToValue(params, Long[].class));

            result.forEach(l -> {

                if (l == null) {
                    throw new IllegalArgumentException("ListId contains null element");
                }
            });
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Jackson param parse error", e);
        }

        return result;
    }

}

package jsonrpc.server.controller.jrpc.base;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import javax.validation.Validator;
import java.util.Arrays;
import java.util.List;

@Service
public abstract class AbstractConverter {

    protected final ObjectMapper objectMapper;
    protected final Validator validator;


    protected AbstractConverter(ObjectMapper objectMapper, Validator validator) {
        this.objectMapper = objectMapper;
        this.validator = validator;
    }


    public Long getId(JsonNode params) {

        Long result;

        // parsing request
        try {
            result = objectMapper.treeToValue(params, Long.class);

            // validate
            if (result == null || result < 0) {
                throw new IllegalArgumentException("id == null < 0");
            }
        }
        // All parse/deserialize errors interpreted as 400 error - do not remove this try/catch
        catch (Exception e) {
            throw new IllegalArgumentException("id parse error", e);
        }
        return result;
    }


    public List<Long> getIdList(JsonNode params) {

        List<Long> result;
        try {

            if (params == null) {
                throw new IllegalArgumentException("IdList == null");
            }


            // https://stackoverflow.com/questions/6349421/how-to-use-jackson-to-deserialise-an-array-of-objects
            result = Arrays.asList(objectMapper.treeToValue(params, Long[].class));

            result.forEach(l -> {

                if (l == null) {
                    throw new IllegalArgumentException("IdList contains null element");
                }
            });
        }
        catch (Exception e) {
            throw new IllegalArgumentException("idList param parse error", e);
        }

        return result;
    }

}

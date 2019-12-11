package jsonrpc.server.entities.base.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pivovarit.function.ThrowingFunction;
import jsonrpc.protocol.dto.base.jrpc.AbstractDto;
import jsonrpc.server.entities.base.AbstractEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import javax.validation.ConstraintViolationException;

@Service
public abstract class AbstractConverter<E extends AbstractEntity,D extends AbstractDto,S> {


    private ThrowingFunction<JsonNode, D, Exception> jsonToDto;
    private ThrowingFunction<JsonNode, S, Exception> jsonToDtoSpec;

    private Function<D, E> toEntity;
    private Function<List<D>, List<E>> toEntityList;
    private Function<E, D> toDto;
    private Function<List<E>, List<D>> toDtoList;

    private   Validator    validator;
    protected ObjectMapper objectMapper;


    @Autowired
    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
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

    public JsonNode toJsonId(AbstractEntity entity) {
        return objectMapper.valueToTree(entity.getId());
    }




    // =================================================================================


    protected void setJsonToDto(ThrowingFunction<JsonNode, D, Exception>  jsonToDto,
                                ThrowingFunction<JsonNode, S, Exception>  jsonToDtoSpec) {

        this.jsonToDto = jsonToDto;
        this.jsonToDtoSpec = jsonToDtoSpec;
    }



    protected void setMappers(
            Function<D, E> toEntity,
            Function<List<D>, List<E>> toEntityList,
            Function<E, D> toDto,
            Function<List<E>, List<D>> toDtoList) {

        this.toEntity = toEntity;
        this.toEntityList = toEntityList;
        this.toDto = toDto;
        this.toDtoList = toDtoList;
    }


    // --------------------------------------------------------------------------


    // Dto => Entity
    public E toEntity(JsonNode params)  {
        try {
            D dto = jsonToDto.apply(params);
            E result = toEntity.apply(dto);
            validate(result);
            return result;
        }
        // It's request, only IllegalArgumentException - will lead to HTTP 400 ERROR
        catch (Exception e) {
            throw new IllegalArgumentException("Jackson parse error:\n" + e.getMessage(), e);
        }
    }


    // Dto => Entity
    public Optional<S> toSpecDto(JsonNode params) {

        try {
            return Optional.ofNullable(jsonToDtoSpec.apply(params));
            // It's request, only IllegalArgumentException - will lead to HTTP 400 ERROR
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Jackson parse error:\n" + e.getMessage(), e);
        }
    }


    // Entity => Dto
    public JsonNode toDtoJson(E entity) {
        D dto = toDto.apply(entity);
        return objectMapper.valueToTree(dto);
    }

    // EntityList => Dto
    public JsonNode toDtoListJson(List<E> entityList) {
        List<D> dtoList = toDtoList.apply(entityList);
        return objectMapper.valueToTree(dtoList);
    }



    private void validate(E entity) {
        Set<ConstraintViolation<E>> violations = validator.validate(entity);
        if (violations.size() != 0) {
            throw new ConstraintViolationException("Entity validation failed", violations);
        }
    }









}

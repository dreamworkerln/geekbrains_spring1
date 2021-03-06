package jsonrpc.resourceserver.entities.base.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pivovarit.function.ThrowingFunction;
import jsonrpc.protocol.dto.base.AbstractDto;
import jsonrpc.resourceserver.entities.base.AbstractEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.util.*;
import java.util.function.Function;
import javax.validation.ConstraintViolationException;

@Service
public abstract class AbstractConverter<E extends AbstractEntity,D extends AbstractDto,S> {


    //private ThrowingFunction<JsonNode, D, Exception> jsonToDto;
    //private ThrowingFunction<JsonNode, S, Exception> jsonToDtoSpec;

    private Function<D, E> toEntity;
    private Function<List<D>, List<E>> toEntityList;
    private Function<E, D> toDto;
    private Function<List<E>, List<D>> toDtoList;

    private   Validator    validator;
    protected ObjectMapper objectMapper;

    protected Class<E> entityClass;
    protected Class<D> dtoClass;
    protected Class<S> specClass;



    @Autowired
    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // Json => Long
    public Long getId(JsonNode params) {

        Long result;

        // parsing request
        try {
            result = objectMapper.treeToValue(params, Long.class);

            // validate
            if (result == null || result < 0) {
                throw new ValidationException("Id validation failed");
            }
        }
        catch (JsonProcessingException e) {
            throw new ParseException(0, "Id parse error", e);
        }
        return result;
    }


    // Json => List<Long>
    public List<Long> getIdList(JsonNode params) {

        List<Long> result;
        try {

            if (params == null) {
                throw new ValidationException("IdList = null");
            }


            // https://stackoverflow.com/questions/6349421/how-to-use-jackson-to-deserialise-an-array-of-objects
            result = Arrays.asList(objectMapper.treeToValue(params, Long[].class));

            result.forEach(l -> {
                if (l == null) {
                    throw new ValidationException("IdList contains null elements");
                }
            });
        }
        catch (JsonProcessingException e) {
            throw new ParseException(0 ,"idList param parse error", e);
        }

        return result;
    }

    // Long => Json
    public JsonNode toIdJson(AbstractEntity entity) {
        return objectMapper.valueToTree(entity.getId());
    }




    // =================================================================================


//    /**
//     * Assign jackson conversion mapping from json to Dto
//     */
//    protected void setJsonToDto(ThrowingFunction<JsonNode, D, Exception>  jsonToDto,
//                                ThrowingFunction<JsonNode, S, Exception>  jsonToDtoSpec) {
//
//        this.jsonToDto = jsonToDto;
//        this.jsonToDtoSpec = jsonToDtoSpec;
//    }

    protected void setClasses(Class<E> entityClass, Class<D> dtoClass, Class<S> specClass) {

        this.entityClass = entityClass;
        this.dtoClass = dtoClass;
        this.specClass = specClass;
    }


    /**
     *  Assign mapstruct mapping from Dto to Entity and vice versa
     */
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


    // Json => Dto => Entity
    public E toEntity(JsonNode params)  {
        try {
            D dto = objectMapper.treeToValue(params, dtoClass);                                               //jsonToDto.apply(params);
            E result = toEntity.apply(dto);
            validate(result);
            return result;
        }
        catch (ValidationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ParseException(0, "Dto parse error", e);
        }
    }


    // Json => Dto (Specifications have no Entities)
    public Optional<S> toSpecDto(JsonNode params) {

        try {
            Optional<S> result = Optional.ofNullable(objectMapper.treeToValue(params, specClass));
            result.ifPresent(this::validateSpecDto);
            return result;
        }
        catch (ValidationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ParseException(0, "To Dto convert error", e);
        }
    }


    // Entity => Dto => Json
    public JsonNode toDtoJson(E entity) {
        try {
            D dto = toDto.apply(entity);
            return objectMapper.valueToTree(dto);
        }
        catch (Exception e) {
            throw new ParseException(0, "To Dto convert error", e);
        }
    }

    // EntityList => Dto => Json
    public JsonNode toDtoListJson(List<E> entityList) {
        try {
            List<D> dtoList = toDtoList.apply(entityList);
            return objectMapper.valueToTree(dtoList);
        }
        catch (Exception e) {
            throw new ParseException(0, "To DtoList parse error", e);
        }
    }


    // check Entity validity
    protected void validate(E entity) {
        Set<ConstraintViolation<E>> violations = validator.validate(entity);
        if (violations.size() != 0) {
            throw new ConstraintViolationException("Entity validation failed", violations);
        }
    }


    // check SpecDto validity
    private void validateSpecDto(S specDto) {
        Set<ConstraintViolation<S>> violations = validator.validate(specDto);
        if (violations.size() != 0) {
            throw new ConstraintViolationException("Entity validation failed", violations);
        }

    }


}

package jsonrpc.server.entities.base.mapper;

import jsonrpc.protocol.dto.product.ProductDto;
import jsonrpc.server.entities.product.Product;
import jsonrpc.server.utils.Utils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.time.Instant;
import java.util.Objects;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public class InstantLongMapper {

    public Long map(Instant i) {

        Long result = null;

        if (Objects.nonNull(i)){
            result = i.getEpochSecond();
        }
        return result;
    }
    

    public Instant map(Long l) {

        Instant result = null;

        if (Objects.nonNull(l)){
            result = Instant.ofEpochSecond(l);
        }
        return result;
    }
}
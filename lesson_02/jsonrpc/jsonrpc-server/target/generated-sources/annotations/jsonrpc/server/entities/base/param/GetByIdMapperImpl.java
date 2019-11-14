package jsonrpc.server.entities.base.param;

import javax.annotation.Generated;
import jsonrpc.protocol.dto.base.param.GetByIdDto;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-11-14T19:09:31+0300",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 1.8.0_222 (Private Build)"
)
@Component
public class GetByIdMapperImpl implements GetByIdMapper {

    @Override
    public GetByIdDto toDto(GetById order) {
        if ( order == null ) {
            return null;
        }

        GetByIdDto getByIdDto = new GetByIdDto();

        getByIdDto.setId( order.getId() );

        return getByIdDto;
    }

    @Override
    public GetById toEntity(GetByIdDto orderDto) {
        if ( orderDto == null ) {
            return null;
        }

        GetById getById = new GetById();

        getById.setId( orderDto.getId() );

        return getById;
    }
}

package jsonrpc.server.entities.base.param;


import jsonrpc.protocol.dto.base.param.GetByIdParamDto;
import jsonrpc.server.entities.base.mapper.InstantLongMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = {InstantLongMapper.class})
public interface GetByIdMapper {

    GetByIdParamDto toDto(GetByIdParam order);
    GetByIdParam toEntity(GetByIdParamDto orderDto);
}

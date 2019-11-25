package jsonrpc.server.entities.storage;

import jsonrpc.protocol.dto.product.ProductDto;
import jsonrpc.protocol.dto.storage.StorageDto;
import jsonrpc.server.entities.base.mapper.InstantLongMapper;
import jsonrpc.server.entities.product.Product;
import jsonrpc.server.entities.product.ProductItemMapper;
import jsonrpc.server.entities.product.ProductMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        //unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {InstantLongMapper.class, ProductItemMapper.class, ProductMapper.class})
public interface StorageMapper {

    StorageDto toDto(Storage storage);
    Storage toEntity(StorageDto storageDto);
}

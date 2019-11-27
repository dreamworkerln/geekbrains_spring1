package jsonrpc.server.entities.order;

import jsonrpc.protocol.dto.client.ClientDto;
import jsonrpc.protocol.dto.manager.ManagerDto;
import jsonrpc.protocol.dto.order.OrderDto;
import jsonrpc.protocol.dto.product.ProductDto;
import jsonrpc.server.entities.Client;
import jsonrpc.server.entities.Manager;
import jsonrpc.server.entities.base.mapper.InstantLongMapper;
import jsonrpc.server.entities.product.Product;
import jsonrpc.server.entities.product.ProductItemMapper;
import jsonrpc.server.entities.product.ProductMapper;
import jsonrpc.server.utils.Utils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = {InstantLongMapper.class, OrderItemMapper.class, ProductMapper.class, ProductItemMapper.class})
        // ProductMapper.class, ProductItemMapper.class, OrderItemMapper.class

public interface OrderMapper {

    //@Mapping(source = "client", target = "client", qualifiedByName = "toClientDto")
    //@Mapping(source = "manager", target = "manager", qualifiedByName = "toManagerDto")
    OrderDto toDto(Order order);
    Order toEntity(OrderDto orderDto);


    //ToDo its a stub, write Interfaces to convert  Client, Manager and add its to uses

    static Client map(ClientDto clientDto) {
        Client result = null;
        return result;
    }



    static ClientDto map(Client client) {
        ClientDto result = null;
        return result;
    }


    static ManagerDto map(Manager client) {
        ManagerDto result = null;
        return result;
    }

    static Manager map(ManagerDto clientDto) {
        Manager result = null;
        return result;
    }


//
//    static List<OrderItem> map(List<OrderItemDto> orderItemDtoList) {
//
//        List<OrderItem> result = new ArrayList<>();
//
//    }



//    @Named("toClientDto")
//    static ClientDto toClientDto(Client client) {
//        ClientDto result = null;
//        return result;
//    }
//
//
//    @Named("toManagerDto")
//    static ManagerDto toManagerDto(Manager manager) {
//        ManagerDto result = null;
//        return result;
//    }

}

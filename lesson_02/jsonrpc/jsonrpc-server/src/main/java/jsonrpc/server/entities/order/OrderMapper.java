package jsonrpc.server.entities.order;

import jsonrpc.protocol.dto.client.ClientDto;
import jsonrpc.protocol.dto.manager.ManagerDto;
import jsonrpc.protocol.dto.order.OrderDto;
import jsonrpc.server.entities.Client;
import jsonrpc.server.entities.Manager;
import jsonrpc.server.entities.base.mapper.InstantLongMapper;
import jsonrpc.server.entities.product.ProductMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        //unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {InstantLongMapper.class, ProductMapper.class, OrderItemMapper.class})
public interface OrderMapper {

    //@Mapping(source = "client", target = "client", qualifiedByName = "toClientDto")
    //@Mapping(source = "manager", target = "manager", qualifiedByName = "toManagerDto")
    OrderDto toDto(Order order);
    Order toEntity(OrderDto orderDto);




    //ToDO its a stub, write Interfaces to convert  Client, Manager and add its to uses
    static ClientDto map(Client client) {
        ClientDto result = null;
        return result;
    }


    static ManagerDto map(Manager client) {
        ManagerDto result = null;
        return result;
    }


    static Client map(ClientDto clientDto) {
        Client result = null;
        return result;
    }


    static Manager map(ManagerDto clientDto) {
        Manager result = null;
        return result;
    }



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

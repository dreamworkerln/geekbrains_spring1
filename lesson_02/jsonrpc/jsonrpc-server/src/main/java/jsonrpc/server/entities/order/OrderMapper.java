package jsonrpc.server.entities.order;

import jsonrpc.protocol.dto.client.ClientDto;
import jsonrpc.protocol.dto.manager.ManagerDto;
import jsonrpc.protocol.dto.order.OrderDto;
import jsonrpc.protocol.dto.order.OrderItemDto;
import jsonrpc.protocol.dto.product.ProductDto;
import jsonrpc.server.entities.Client;
import jsonrpc.server.entities.Manager;
import jsonrpc.server.entities.base.mapper.InstantLongMapper;
import jsonrpc.server.entities.product.Product;
import jsonrpc.server.entities.product.ProductItemMapper;
import jsonrpc.server.entities.product.ProductMapper;
import jsonrpc.server.repository.ProductRepository;
import jsonrpc.server.utils.Utils;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = {InstantLongMapper.class, OrderItemMapper.class, ProductMapper.class, ProductItemMapper.class})
        // ProductMapper.class, ProductItemMapper.class, OrderItemMapper.class

public abstract class OrderMapper {


    @Autowired
    ProductRepository productRepository;

    //@Mapping(source = "client", target = "client", qualifiedByName = "toClientDto")
    //@Mapping(source = "manager", target = "manager", qualifiedByName = "toManagerDto")
    public abstract OrderDto toDto(Order order);
    public abstract Order toEntity(OrderDto orderDto);
    //Order toEntity(OrderDto orderDto, @Context ProductRepository productRepository);


    //ToDo its a stub, write Interfaces to convert  Client, Manager and add its to uses

    Client map(ClientDto clientDto) {
        Client result = null;
        return result;
    }

    ClientDto map(Client client) {
        ClientDto result = null;
        return result;
    }

    ManagerDto map(Manager client) {
        ManagerDto result = null;
        return result;
    }

    Manager map(ManagerDto clientDto) {
        Manager result = null;
        return result;
    }

//    @AfterMapping
//    default void postMap(OrderDto source, @MappingTarget Order target,@Context ProductRepository productRepository) {


    @AfterMapping
    void postMap(OrderDto source, @MappingTarget Order target) {

        // 1. Set Order.id
        Utils.idSetter(target, source.getId());

        // 2. Fetch all persistedProducts
        target.getItemList().forEach(oi -> {

            Product p = oi.getProduct();
            Product persistedProduct = productRepository.getById(p.getId());

            if (persistedProduct == null) {
                throw new IllegalArgumentException("Product not persisted");
            }
            oi.setProduct(persistedProduct);
            oi.setOrder(target);  // Set owner to all OrderItems
        });
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

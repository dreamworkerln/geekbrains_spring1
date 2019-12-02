package jsonrpc.server.entities.order.mappers;

import jsonrpc.protocol.dto.client.ClientDto;
import jsonrpc.protocol.dto.manager.ManagerDto;
import jsonrpc.protocol.dto.order.OrderDto;
import jsonrpc.server.entities.Client;
import jsonrpc.server.entities.Manager;
import jsonrpc.server.entities.base.mapper.InstantMapper;
import jsonrpc.server.entities.order.Order;
import jsonrpc.server.entities.order.mappers.OrderItemMapper;
import jsonrpc.server.entities.product.Product;
import jsonrpc.server.entities.product.mappers.ProductItemMapper;
import jsonrpc.server.entities.product.mappers.ProductMapper;
import jsonrpc.server.repository.ProductRepository;
import jsonrpc.utils.Utils;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = {InstantMapper.class, OrderItemMapper.class, ProductMapper.class, ProductItemMapper.class})
// ProductMapper.class, ProductItemMapper.class, OrderItemMapper.class

public abstract class OrderMapper {


    @Autowired
    ProductRepository productService;

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
    }


//    @AfterMapping
//    void postMap(OrderDto source, @MappingTarget Order target) {
//
//        // 1. Set Order.id
//        Utils.idSetter(target, source.getId());
//
//        // 2. Fetch all persistedProducts
//        target.getItemList().forEach(item -> {
//
//            Product p = item.getProduct();
//            Product persistedProduct = productService.findById(p.getId()).orElse(null); // )))
//
//            if (persistedProduct == null) {
//                throw new IllegalArgumentException("Product not persisted");
//            }
//            item.setProduct(persistedProduct);
//            item.setOrder(target);  // Set owner to all OrderItems
//        });
//    }


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

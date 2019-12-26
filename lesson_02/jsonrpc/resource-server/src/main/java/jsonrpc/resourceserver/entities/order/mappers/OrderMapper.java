package jsonrpc.resourceserver.entities.order.mappers;

import jsonrpc.protocol.dto.client.ClientDto;
import jsonrpc.protocol.dto.manager.ManagerDto;
import jsonrpc.protocol.dto.order.OrderDto;
import jsonrpc.protocol.dto.order.OrderItemDto;
import jsonrpc.resourceserver.entities.Client;
import jsonrpc.resourceserver.entities.Manager;
import jsonrpc.resourceserver.entities.base.mapper.AbstractMapper;
import jsonrpc.resourceserver.entities.base.mapper.InstantMapper;
import jsonrpc.resourceserver.entities.order.Order;
import jsonrpc.resourceserver.entities.order.OrderItem;
import jsonrpc.resourceserver.entities.product.mappers.ProductMapper;
import jsonrpc.resourceserver.service.order.OrderService;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;


@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = {InstantMapper.class, ProductMapper.class})
// ProductMapper.class, ProductItemMapper.class, OrderItemMapper.class
public abstract class OrderMapper extends AbstractMapper {

    @Autowired
    OrderService orderService;

    //@Mapping(source = "client", target = "client", qualifiedByName = "toClientDto")
    //@Mapping(source = "manager", target = "manager", qualifiedByName = "toManagerDto")


    //@Mapping(source = "created", target = "created", qualifiedByName = "toCreated")
    public abstract OrderDto toDto(Order order);

    public abstract Order toEntity(OrderDto orderDto);
    //OrderN toItemEntity(OrderDto orderDto, @Context ProductRepository productRepository);


    @Mapping(source = "product.id", target = "productId"/*, qualifiedByName = "toProductDto"*/)
    public abstract OrderItemDto toItemDto(OrderItem orderItem);


    @Mapping(target = "order", ignore = true)
    @Mapping(source = "productId", target = "product"/*, qualifiedByName = "toProduct"*/)
    public abstract OrderItem toItemEntity(OrderItemDto orderItemDto);
    


    //ToDo its a stub, write Interfaces to convert  Client, Manager and add its to uses
    Client map(ClientDto clientDto) {
        return null;
    }

    ClientDto map(Client client) {
        return null;
    }

    ManagerDto map(Manager manager) {
        return null;
    }

    Manager map(ManagerDto managerDto) {
        return null;
    }




//    @AfterMapping
//    default void postMap(OrderDto source, @MappingTarget OrderN target,@Context ProductRepository productRepository) {

    
    @AfterMapping
    void afterMapping(OrderDto source, @MappingTarget Order target) {
        idMap(orderService::findById, source, target);
    }

    @AfterMapping
    void afterMapping(OrderItemDto source, @MappingTarget OrderItem target) {

        idMap(orderService::findItemById, source, target);
    }







        /*

        // 2. подгружаем OrderItem, Products
        List<Long> productIdList = new ArrayList<>();
        //List<Long> orderItemIdList = new ArrayList<>();

        target.getItemList().forEach(item -> {

            productIdList.add(item.getProduct().getId());
            //orderItemIdList.add(item.getId());
        });

        // Список типов товаров, которые есть в наличии в базе
        Map<Long, product> productMap = productService.findAllById(productIdList).stream()
                .collect(Collectors.toMap(AbstractEntity::getId, product -> product));

        // OrderItem, которые уже есть в наличии в базе для update-запроса заказа (созданы в прошлый раз)
        Map<Long, OrderItem> orderItemMap = new HashMap<>();


        Optional<OrderN> pOrder = Optional.empty();
        if (target.getId() != null) {
            pOrder = orderService.findById(target.getId());
        }
        if (pOrder.isPresent()) {
            orderItemMap = pOrder.get().getItemList().stream()
                    .collect(Collectors.toMap(AbstractEntity::getId, orderItem -> orderItem));
        }


        // Патчим OrderN.itemList - >
        // подшиваем OrderItem.created, OrderItem.updated и OrderItem.product вручную
        // (Так к нам на вход приезжает довольно обгрызанный OrderDto, где таких данных просто нет)
        Map<Long, OrderItem> fOrderItemMap = orderItemMap;
        target.getItemList().forEach(item -> {

            // прицепляем product
            if (!productMap.containsKey(item.getProduct().getId())) {
                throw new IllegalArgumentException("OrderN product not persisted");
            }
            item.setProduct(productMap.get(item.getProduct().getId()));

            // прицепляем OrderItem.created, OrderItem.updated
            if (fOrderItemMap.containsKey(item.getId())) {
                Utils.fieldSetter("created", item, fOrderItemMap.get(item.getId()).getCreated());
                Utils.fieldSetter("updated", item, fOrderItemMap.get(item.getId()).getUpdated());
            }

            // прицепляем OrderN
            item.setOrder(target);
        });

        */





//        // 1. Set id
//        Utils.fieldSetter("id", target, source.getId());
//
//        // 2. Set created and updated from resourceserver
//        if (target.getId() != null) {
//            OrderN persisted = productService.findById(entity.getId()).orElse(null);
//
//            if (persisted!= null) {
//                Utils.fieldSetter("created", entity, persisted.getCreated());
//                //Utils.fieldSetter("updated", entity, persisted.getUpdated());
//            }
//        }



        // Do not map created & updated - only resourceserver produce this values

        // Utils.fieldSetter("created", target, source.getCreated());
        // Utils.fieldSetter("updated", target, source.getUpdated());



//    @AfterMapping
//    void postMap(OrderDto source, @MappingTarget OrderN target) {
//
//        // 1. Set OrderN.id
//        Utils.idSetter(target, source.getId());
//
//        // 2. Fetch all persistedProducts
//        target.getItemList().forEach(item -> {
//
//            product p = item.getProduct();
//            product persistedProduct = productService.findById(p.getId()).orElse(null); // )))
//
//            if (persistedProduct == null) {
//                throw new IllegalArgumentException("product not persisted");
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

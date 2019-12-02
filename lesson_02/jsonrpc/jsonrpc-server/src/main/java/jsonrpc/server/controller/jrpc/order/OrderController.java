package jsonrpc.server.controller.jrpc.order;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jsonrpc.protocol.dto.base.HandlerName;
import jsonrpc.protocol.dto.order.OrderDto;
import jsonrpc.server.controller.jrpc.base.JrpcMethod;
import jsonrpc.server.entities.order.Order;
import jsonrpc.server.entities.order.mappers.OrderMapper;
import jsonrpc.server.controller.jrpc.base.AbstractJrpcController;
import jsonrpc.server.controller.jrpc.base.JrpcController;
import jsonrpc.server.entities.product.Product;
import jsonrpc.server.repository.OrderRepository;
import jsonrpc.server.service.OrderService;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

/**
 * Выдает информацию о товарах<br>
 * Какие типы товаров есть, их описание, цена, артикул и т.д.
 * (За количеством на складе обращайтесь в сервис Storage)
 */
@Service
@JrpcController(path = HandlerName.Order.path)
public class OrderController extends AbstractJrpcController {

    private final static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final OrderService orderService;
    private final OrderMapper orderMapper;


    @Autowired
    protected OrderController(ObjectMapper objectMapper,
                              OrderService orderService,
                              OrderMapper orderMapper) {

        super(objectMapper);

        this.orderService = orderService;
        this.orderMapper = orderMapper;
    }

    
    // -----------------------------------------------------------------------------



    @JrpcMethod(method = HandlerName.Order.findById)
    public JsonNode findById(JsonNode params) {

        // request id
        long id = getId(params);

        // Getting from repository product by id
        Order order = orderService.findById(id).orElse(null);
        OrderDto orderdto = orderMapper.toDto(order);
        return objectMapper.valueToTree(orderdto);
    }

    @JrpcMethod(method = HandlerName.Order.save)
    public JsonNode save(JsonNode params) {

        Order order = getOrder(params);
        order = orderService.save(order);
        return objectMapper.valueToTree(order.getId());
    }


    @JrpcMethod(method = HandlerName.Order.delete)
    public JsonNode delete(JsonNode params) {

        Order order = getOrder(params);
        orderService.delete(order);
        return null;
    }



    // ==============================================================================






    private Order getOrder(JsonNode params) {

        // parsing request
        Order result;
        try {
            OrderDto orderDto = objectMapper.treeToValue(params, OrderDto.class);

            result = orderMapper.toEntity(orderDto);
            // Проверяем на валидность
            orderService.validate(result);

        }
        catch (Exception e) {
            throw new IllegalArgumentException("Jackson parse error:\n" + e.getMessage(), e);
        }
        return result;
    }




    // https://amydegregorio.com/2018/05/23/skipping-fields-with-modelmapper/
    //
    // by lambdas
    // https://stackoverflow.com/questions/49003929/how-to-use-explicit-map-with-java-8-and-modelmapper

//
//    @Override
//    protected void setMappings() {
//
//
//// Create type converter for converting final types (for which no proxies can be used by ModelMapper)
//
////        Converter<Instant, Long> toEpochSecond = context -> context.getSource() ==
////                                                            null ? null : context.getSource().getEpochSecond();//
////        Converter<Long, Instant> toInstant = context -> context.getSource() ==
////                                                        null ? null : Instant.ofEpochMilli(context.getSource());
//        //modelMapper.addConverter(toEpochSecond);
//
//
//
//
//        System.out.println("Displaying available type mappings:");
//        modelMapper.getTypeMaps().forEach(System.out::println);
//        System.out.println("-----------------------------------\n");
//
//
///*        modelMapper.addMappings(new PropertyMap<OrderDto, Order>() {
//            @Override
//            protected void configure() {
//                skip().setDate(null);
//                map(source.getItemList(), destination::setItemList)
//                //map().getItems().forEach(item -> item.setOrder(destination));
//            }
//        });
//
//        modelMapper.addMappings(new PropertyMap<Order, OrderDto>() {
//            @Override
//            protected void configure() {
//                skip().setDate(null);
//            }
//        });*/
//
//
//        //TypeMap<OrderItemDto, OrderItem> typeMap =  modelMapper.createTypeMap(OrderItemDto.class, OrderItem.class);
//
//
////        modelMapper.addMappings(new PropertyMap<OrderItemDto, OrderItem>() {
////            @Override
////            protected void configure() {
////                map().setOrder(this);
////            }
////        });
//
////        modelMapper.addMappings(new PropertyMap<OrderItem, OrderDto>() {
////            @Override
////            protected void configure() {
////                skip().setDate(null);
////            }
////        });
//    }


    // https://www.baeldung.com/entity-to-and-from-dto-for-a-java-spring-application
    // convert DTO from client to Entity



//    private OrderDto convertToDto(Order order) {
//
//        OrderDto result = modelMapper.map(order, OrderDto.class);
//        // result.setDate(order.getDate().getEpochSecond());
//        return result;
//    }
//
//    private Order convertToEntity(OrderDto orderDto) {
//
//        Order result = modelMapper.map(orderDto, Order.class);
//        //result.setDate(Instant.ofEpochSecond(orderDto.getDate()));
//
//        return result;
//    }














}

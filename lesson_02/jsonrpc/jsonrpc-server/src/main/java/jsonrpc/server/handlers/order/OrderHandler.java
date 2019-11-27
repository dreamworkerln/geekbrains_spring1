package jsonrpc.server.handlers.order;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jsonrpc.protocol.dto.base.HandlerName;
import jsonrpc.protocol.dto.base.jrpc.AbstractDto;
import jsonrpc.protocol.dto.base.param.IdDto;
import jsonrpc.protocol.dto.order.OrderDto;
import jsonrpc.server.entities.order.Order;
import jsonrpc.server.entities.order.OrderMapper;
import jsonrpc.server.handlers.base.HandlerBase;
import jsonrpc.server.handlers.base.JrpcController;
import jsonrpc.server.handlers.base.JrpcHandler;
import jsonrpc.server.repository.OrderRepository;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;

/**
 * Выдает информацию о товарах.<br>
 * Какие типы товаров есть, их описание, артикул и т.д.
 * (За количеством на складе обращайтесь в сервис Storage)
 */
@Service
@JrpcController(path = HandlerName.Order.path)
public class OrderHandler extends HandlerBase {

    private final static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;


    @Autowired
    protected OrderHandler(ObjectMapper objectMapper,
                           OrderRepository orderRepository,
                           OrderMapper orderMapper) {

        super(objectMapper);

        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    
    // -----------------------------------------------------------------------------



    @JrpcHandler(method = HandlerName.Order.getById)
    public AbstractDto getById(JsonNode params) {

        // request id
        long id = getId(params);

        // Getting from repository product by id
        Order order = orderRepository.getById(id);

        return orderMapper.toDto(order);
    }


    @JrpcHandler(method = HandlerName.Order.put)
    public AbstractDto put(JsonNode params) {

        Order order = getOrder(params);
        orderRepository.put(order);
        return new IdDto(order.getId());
    }


    @JrpcHandler(method = HandlerName.Order.delete)
    public AbstractDto delete(JsonNode params) {

        throw new NotImplementedException("ProductHandler.delete");
    }



    // ==============================================================================






    private Order getOrder(JsonNode params) {

        // parsing request
        Order result;
        try {
            OrderDto orderDto = objectMapper.treeToValue(params, OrderDto.class);
            OrderDto.validate(orderDto);
            result = orderMapper.toEntity(orderDto);
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Jackson parse error", e);
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

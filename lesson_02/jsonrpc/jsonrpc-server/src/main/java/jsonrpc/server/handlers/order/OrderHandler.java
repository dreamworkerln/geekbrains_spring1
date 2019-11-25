package jsonrpc.server.handlers.order;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jsonrpc.protocol.dto.base.jrpc.AbstractDto;
import jsonrpc.protocol.dto.order.OrderDto;
import jsonrpc.server.entities.base.param.GetById;
import jsonrpc.server.entities.base.param.GetByIdMapper;
import jsonrpc.server.entities.order.Order;
import jsonrpc.server.entities.order.OrderMapper;
import jsonrpc.server.handlers.base.HandlerBase;
import jsonrpc.server.handlers.base.JrpcController;
import jsonrpc.server.handlers.base.JrpcHandler;
import jsonrpc.server.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;

import static jsonrpc.server.configuration.SpringConfiguration.MAIN_ENTITIES_PATH;


@Service
@JrpcController(path = MAIN_ENTITIES_PATH + "." + "order")
public class OrderHandler extends HandlerBase {

    private final static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Autowired
    protected OrderHandler(ObjectMapper objectMapper,
                           GetByIdMapper getByIdMapper,
                           OrderRepository orderRepository,
                           OrderMapper orderMapper) {

        super(objectMapper, getByIdMapper);

        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }


    @JrpcHandler(method = "getById")
    public AbstractDto getById(JsonNode params) {

        OrderDto result;

        // parsing request
        GetById request = getByIdRequest(params);


        // Getting from repository order by id
        Order order = orderRepository.getById(request.getId());

        try {

            result = orderMapper.toDto(order);

        } catch (Exception e) {
            throw new RuntimeException("MapStruct error", e);
        }
        return result;
    }


    @JrpcHandler(method = "put")
    public AbstractDto put(JsonNode params) {

        //PutOrder request;
        Order order;
        try {
            //PutOrderDto requestDto = objectMapper.treeToValue(params, PutOrderDto.class);
            //request = putOrderMapper.toEntity(requestDto);
            OrderDto requestDto = objectMapper.treeToValue(params, OrderDto.class);
            order = orderMapper.toEntity(requestDto);

        }
        // All parse/deserialize errors interpret as 400 error
        catch (Exception e) {
            throw new IllegalArgumentException("Jackson parse error", e);
        }

        // Adding to repository order by id
        orderRepository.add(order);

        return null;
    }

    // ===============================================



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

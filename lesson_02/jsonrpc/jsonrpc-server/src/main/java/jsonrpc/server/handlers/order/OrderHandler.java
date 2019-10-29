package jsonrpc.server.handlers.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jsonrpc.protocol.dto.order.OrderDto;
import jsonrpc.protocol.dto.base.param.GetById;
import jsonrpc.protocol.dto.base.jrpc.JrpcResponse;
import jsonrpc.server.entities.Order;
import jsonrpc.server.handlers.base.JrpcController;
import jsonrpc.server.handlers.base.JrpcHandler;
import jsonrpc.server.handlers.base.MethodHandlerBase;
import jsonrpc.server.repository.OrderRepository;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.time.Instant;

import static jsonrpc.server.configuration.SpringConfiguration.MAIN_ENTITIES_PATH;


@Service
@JrpcController(path = MAIN_ENTITIES_PATH + "." + "order")
public class OrderHandler extends MethodHandlerBase {

    private final static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final OrderRepository orderRepository;

    protected OrderHandler(ObjectMapper objectMapper, ModelMapper modelMapper, OrderRepository orderRepository) {
        super(objectMapper, modelMapper);
        this.orderRepository = orderRepository;
    }


    @JrpcHandler(method = "getById")
    public JrpcResponse getById(JsonNode params) {

        OrderDto result;
        GetById request;

        try {
            request = objectMapper.treeToValue(params, GetById.class);
        } catch (JsonProcessingException e) {
            log.error("json parse error: " + params.toPrettyString(), e);
            throw new IllegalArgumentException(e);
        }

        // Getting from repository order by "id"
        Order order = orderRepository.getById(request.getId());

        try {

            result = modelMapper.map(order, OrderDto.class);
            
//          Order order2 = modelMapper.map(result, Order.class);
//
//          log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result));
//          Order order2 = convertToEntity(result);
//          System.out.println(order2);



        } catch (Exception e) {
            log.error("ModelMapper error", e);
            throw new IllegalArgumentException(e);
        }
        return result;
    }

    // ===============================================



    // https://amydegregorio.com/2018/05/23/skipping-fields-with-modelmapper/
    //
    // by lambdas
    // https://stackoverflow.com/questions/49003929/how-to-use-explicit-map-with-java-8-and-modelmapper
    @Override
    protected void setMappings() {


// Create type converter for converting final types (for which no proxies can be used by ModelMapper)

//        Converter<Instant, Long> toEpochSecond = context -> context.getSource() ==
//                                                            null ? null : context.getSource().getEpochSecond();//
        Converter<Long, Instant> toInstant = context -> context.getSource() ==
                                                        null ? null : Instant.ofEpochMilli(context.getSource());
        //modelMapper.addConverter(toEpochSecond);


        modelMapper.createTypeMap(Order.class, OrderDto.class).addMappings(
                mapper -> {

                    //mapper.map(src -> src.getDate().getEpochSecond(), OrderDto::setDate);
                    mapper.skip(OrderDto::setDate);
                }).setPostConverter(getToDtoConverter());

        modelMapper.createTypeMap(OrderDto.class, Order.class).addMappings(mapper -> {

            mapper.skip(Order::setDate);
            //mapper.map(src -> Instant.ofEpochSecond(src.getDate()), Order::setDate);
        });

        modelMapper.getTypeMaps().forEach(System.out::println);



/*        modelMapper.addMappings(new PropertyMap<OrderDto, Order>() {
            @Override
            protected void configure() {
                skip().setDate(null);
                map(source.getItemList(), destination::setItemList)
                //map().getItems().forEach(item -> item.setOrder(destination));
            }
        });

        modelMapper.addMappings(new PropertyMap<Order, OrderDto>() {
            @Override
            protected void configure() {
                skip().setDate(null);
            }
        });*/


        //TypeMap<OrderItemDto, OrderItem> typeMap =  modelMapper.createTypeMap(OrderItemDto.class, OrderItem.class);


//        modelMapper.addMappings(new PropertyMap<OrderItemDto, OrderItem>() {
//            @Override
//            protected void configure() {
//                map().setOrder(this);
//            }
//        });

//        modelMapper.addMappings(new PropertyMap<OrderItem, OrderDto>() {
//            @Override
//            protected void configure() {
//                skip().setDate(null);
//            }
//        });
    }


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

    // ModelMapper: путешествие туда и обратно
    // https://habr.com/ru/post/438808/
    //
    // https://github.com/promoscow/modelmapper-demo



    private Converter<Order, OrderDto> getToDtoConverter() {
        return context -> {
            Order source = context.getSource();
            OrderDto destination = context.getDestination();
            mapSpecificFields(source, destination);
            return context.getDestination();
        };
    }

    private Converter<OrderDto, Order> getToEntityConverter() {
        return context -> {
            OrderDto source = context.getSource();
            Order destination = context.getDestination();
            mapSpecificFields(source, destination);
            return context.getDestination();
        };
    }





    private void mapSpecificFields(Order source, OrderDto destination) {
        destination.setDate(source.getDate().getEpochSecond());
    }

    private void mapSpecificFields(OrderDto  source, Order destination) {
        destination.setDate(Instant.ofEpochSecond(source.getDate()));
    }


}

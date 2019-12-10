package jsonrpc.server.controller.jrpc.order;

import com.fasterxml.jackson.databind.JsonNode;
import jsonrpc.protocol.dto.base.HandlerName;
import jsonrpc.protocol.dto.order.OrderDto;
import jsonrpc.server.controller.jrpc.base.AbstractConverter;
import jsonrpc.server.controller.jrpc.base.JrpcMethod;
import jsonrpc.server.entities.order.Order;
import jsonrpc.server.entities.order.mappers.OrderMapper;
import jsonrpc.server.controller.jrpc.base.JrpcController;
import jsonrpc.server.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.lang.invoke.MethodHandles;
import java.util.Set;

/**
 * Выдает информацию о товарах<br>
 * Какие типы товаров есть, их описание, цена, артикул и т.д.
 * (За количеством на складе обращайтесь в сервис Storage)
 */
@Service
@JrpcController(path = HandlerName.Order.path)
public class OrderController  {

    private final static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final OrderService orderService;
    private final OrderConverter converter;


    @Autowired
    protected OrderController(OrderService orderService,
                              OrderConverter converter) {

        this.orderService = orderService;
        this.converter = converter;
    }


    // -----------------------------------------------------------------------------



    @JrpcMethod(method = HandlerName.Order.findById)
    public JsonNode findById(JsonNode params) {

        long id = converter.getId(params);
        Order order = orderService.findById(id).orElse(null);
        return converter.toOrderDtoJson(order);
    }


    @JrpcMethod(method = HandlerName.Order.save)
    public JsonNode save(JsonNode params)  {

        Order order = converter.toOrder(params);
        order = orderService.save(order);

        //order = orderService.findById(order.getId()).get();
        System.out.println(order);

        return converter.toJsonId(order);
    }


    @JrpcMethod(method = HandlerName.Order.delete)
    public JsonNode delete(JsonNode params)  {

        Order order = converter.toOrder(params);
        orderService.delete(order);
        return null;
    }


    // ==============================================================================



    // Вообщем сделал базовый репозиторий, который умеет делать
    // void refresh(Entity entity)
    //
    // Итого в сервисе:
    //
    // Entity saved = repository.save(Entity entity)
    // Entity refreshed = EntityManager.refresh(Entity saved)
    // return refreshed;
    //
    // соответственно после сохранения сущность из базы приезжает со всем графом дочерних объектов

    // ToDo: generify OrderConverter(or AbstractConverter) and use it for other converters?

    @Service
    static class OrderConverter extends AbstractConverter {

        private final OrderMapper orderMapper;

        public OrderConverter(OrderMapper orderMapper) {
            this.orderMapper = orderMapper;
        }

        // Dto => Entity
        Order toOrder(JsonNode params)  {

            // parsing request
            try {
                OrderDto dto = objectMapper.treeToValue(params, OrderDto.class);
                Order result = orderMapper.toEntity(dto);
                // Проверяем на валидность
                validate(result);
                return result;
            }
            // It's request, only IllegalArgumentException - will lead to HTTP 400 ERROR
            catch (Exception e) {
                throw new IllegalArgumentException("Jackson parse error:\n" + e.getMessage(), e);
            }
        }

        // Entity => Dto
        JsonNode toOrderDtoJson(Order order) {
            OrderDto orderDto = orderMapper.toDto(order);
            return objectMapper.valueToTree(orderDto);
        }


        // Валидатор валидирует только объект верхнего уровня
        // Дочерние объекты проверять не будет (ходить по графу)
        void validate(Order order) {

            Set<ConstraintViolation<Order>> violations = validator.validate(order);
            if (violations.size() != 0) {
                log.error("Given order: {}", order);
                throw new ConstraintViolationException("Order validation failed", violations);
            }
        }
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

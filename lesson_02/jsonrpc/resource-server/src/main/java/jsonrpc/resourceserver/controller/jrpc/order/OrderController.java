package jsonrpc.resourceserver.controller.jrpc.order;

import com.fasterxml.jackson.databind.JsonNode;
import jsonrpc.protocol.dto.base.HandlerName;
import jsonrpc.resourceserver.controller.jrpc.base.JrpcMethod;
import jsonrpc.resourceserver.entities.order.Order;
import jsonrpc.resourceserver.entities.order.mappers.OrderConverter;
import jsonrpc.resourceserver.controller.jrpc.base.JrpcController;
import jsonrpc.resourceserver.service.order.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;

/**
 * Выдает информацию о товарах<br>
 * Какие типы товаров есть, их описание, цена, артикул и т.д.
 * (За количеством на складе обращайтесь в сервис StorageN)
 */
@Service
@JrpcController(path = HandlerName.OrderN.path)
public class OrderController  {

    private final static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final OrderService orderService;
    private final OrderConverter converter;


    @Autowired
    protected OrderController(OrderService orderService, OrderConverter converter) {

        this.orderService = orderService;
        this.converter = converter;
    }


    // -----------------------------------------------------------------------------



    @JrpcMethod(method = HandlerName.OrderN.findById)
    public JsonNode findById(JsonNode params) {

        long id = converter.getId(params);
        Order order = orderService.findById(id).orElse(null);
        return converter.toDtoJson(order);
    }


    @JrpcMethod(method = HandlerName.OrderN.save)
    public JsonNode save(JsonNode params)  {

        Order order = converter.toEntity(params);
        order = orderService.save(order);

        //order = orderService.findById(order.getId()).get();
        System.out.println(order);

        return converter.toIdJson(order);
    }


    @JrpcMethod(method = HandlerName.OrderN.delete)
    public JsonNode delete(JsonNode params)  {

        Order order = converter.toEntity(params);
        orderService.delete(order);
        return null;
    }

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



//    @Service
//    static class OrderConverter extends AbstractConverter {
//
//        private final OrderMapper orderMapper;
//
//        public OrderConverter(OrderMapper orderMapper) {
//            this.orderMapper = orderMapper;
//        }
//
//        // Dto => Entity
//        OrderN toOrder(JsonNode params)  {
//
//            // parsing request
//            try {
//                OrderDto dto = objectMapper.jsonToDto(params, OrderDto.class);
//                OrderN result = orderMapper.toItemEntity(dto);
//                // Проверяем на валидность
//                validate(result);
//                return result;
//            }
//            // It's request, only IllegalArgumentException - will lead to HTTP 400 ERROR
//            catch (Exception e) {
//                throw new IllegalArgumentException("Jackson parse error:\n" + e.getMessage(), e);
//            }
//        }
//
//        // Entity => Dto
//        JsonNode toOrderDtoJson(OrderN order) {
//            OrderDto orderDto = orderMapper.toItemDto(order);
//            return objectMapper.valueToTree(orderDto);
//        }
//
//
//        // Валидатор валидирует только объект верхнего уровня
//        // Дочерние объекты проверять не будет (ходить по графу)
//        void validate(OrderN order) {
//
//            Set<ConstraintViolation<OrderN>> violations = validator.validate(order);
//            if (violations.size() != 0) {
//                log.body("Given order: {}", order);
//                throw new ConstraintViolationException("OrderN validation failed", violations);
//            }
//        }
//    }




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
///*        modelMapper.addMappings(new PropertyMap<OrderDto, OrderN>() {
//            @Override
//            protected void configure() {
//                skip().setDate(null);
//                map(source.getItemList(), destination::setItemList)
//                //map().getItems().forEach(item -> item.setOrder(destination));
//            }
//        });
//
//        modelMapper.addMappings(new PropertyMap<OrderN, OrderDto>() {
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



//    private OrderDto convertToDto(OrderN order) {
//
//        OrderDto result = modelMapper.map(order, OrderDto.class);
//        // result.setDate(order.getDate().getEpochSecond());
//        return result;
//    }
//
//    private OrderN convertToEntity(OrderDto orderDto) {
//
//        OrderN result = modelMapper.map(orderDto, OrderN.class);
//        //result.setDate(Instant.ofEpochSecond(orderDto.getDate()));
//
//        return result;
//    }





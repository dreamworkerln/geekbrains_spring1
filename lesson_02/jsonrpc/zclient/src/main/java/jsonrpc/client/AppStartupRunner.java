package jsonrpc.client;

import jsonrpc.client.configuration.ClientProperties;
import jsonrpc.client.request.OrderRequest;
import jsonrpc.client.request.ProductRequest;
import jsonrpc.client.request.StorageRequest;
import jsonrpc.protocol.dto.base.filter.specification.ProductSpecDto;
import jsonrpc.protocol.dto.order.OrderDto;
import jsonrpc.protocol.dto.order.OrderItemDto;
import jsonrpc.protocol.dto.product.ProductDto;
import jsonrpc.protocol.dto.product.ProductItemDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class AppStartupRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    private final ClientProperties clientProperties;
    private final ProductRequest productRequest;
    private final StorageRequest storageRequest;
    private final OrderRequest orderRequest;


    @Autowired
    public AppStartupRunner(ClientProperties clientProperties,
                            ProductRequest productRequest,
                            StorageRequest storageRequest,
                            OrderRequest orderRequest) {

        this.clientProperties = clientProperties;
        this.productRequest = productRequest;
        this.storageRequest = storageRequest;
        this.orderRequest = orderRequest;
    }



    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        /*
        System.out.println("Завезем на склад 500 единиц товара с id=2:\n");
        storageRequest.put(2L, 500);
        System.out.println("\n");
        */

/*
        System.out.println("Сделаем заказ:\n");
        OrderDto orderDto = new OrderDto();
        orderDto.addItem(new OrderItemDto(1L, 3));
        orderDto.addItem(new OrderItemDto(2L, 4));
        orderDto.addItem(new OrderItemDto(3L, 5));
        orderDto.addItem(new OrderItemDto(4L, 6));
        orderDto.setStatus(OrderDto.Status.QUEUED);

        Long orderId = orderRequest.save(orderDto);
        System.out.println("orderId: " + orderId);
        System.out.println("\n");
*/



        System.out.println("Using client config: " + clientProperties.getServer());
        System.out.println("\n");
        List<ProductDto> productDtoList;


        System.out.println("Список товаров:\n");
        productDtoList = productRequest.findAll(null);
        System.out.println(productDtoList);
        System.out.println("\n");


        System.out.println("Список товаров с ценой от 0 до 50 категории [1], цена ASC:\n");
        ProductSpecDto spec = new ProductSpecDto();
        spec.getCategoryList().add(1L);
        spec.getCategoryList().add(2L);
        spec.setPriceMin(BigDecimal.valueOf(0));
        spec.setPriceMax(BigDecimal.valueOf(50));
        spec.setPriceOrderBy(ProductSpecDto.OrderBy.ASC);
        productDtoList = productRequest.findAll(spec);
        System.out.println(productDtoList);
        System.out.println("\n");


        System.out.println("Список товаров с ценой выше 30:\n");
        spec = new ProductSpecDto();
        spec.setPriceMin(BigDecimal.valueOf(30));
        productDtoList = productRequest.findAll(spec);
        System.out.println(productDtoList);
        System.out.println("\n");

        System.out.println("Список товаров по 3 элемента\n");
        spec = new ProductSpecDto();
        spec.setLimit(3);
        productDtoList = productRequest.findFirst(spec);
        System.out.println(productDtoList);
        System.out.println("\n");


        System.out.println("Запасы на складе:\n");
        List<ProductItemDto> productItemDtoList = storageRequest.getAll();
        System.out.println(productItemDtoList);
        System.out.println("\n");


        System.out.println("Завезем на склад 500 единиц товара с id=2:\n");
        storageRequest.put(2L, 500);
        System.out.println("\n");


        System.out.println("Запасы товара id=2 на складе:\n");
        ProductItemDto productItemDto = storageRequest.getById(2L);
        System.out.println(productItemDto);
        System.out.println("\n");


        try {
            System.out.println("Попытаемся забрать со склада 9999 единиц товара с id=2:\n");
            storageRequest.remove(2L, 9999);
        } catch (HttpStatusCodeException e) {
            log.error("HTTP " + e.getStatusCode().toString() +"\n" +
                      new String(e.getResponseBodyAsByteArray(),StandardCharsets.UTF_8.name()));
            //System.out.println("JRPC ERROR: " + objectMapper.readTree(e.getResponseBodyAsString()).get("error"));
        }



        System.out.println("Сделаем заказ:\n");
        OrderDto orderDto = new OrderDto();
        orderDto.addItem(new OrderItemDto(1L, 1));
        orderDto.addItem(new OrderItemDto(2L, 2));
        orderDto.addItem(new OrderItemDto(3L, 3));
        orderDto.addItem(new OrderItemDto(4L, 4));
        orderDto.setStatus(OrderDto.Status.QUEUED);

        Long orderId = orderRequest.save(orderDto);
        System.out.println("orderId: " + orderId);
        System.out.println("\n");


        System.out.println("Проверим заказ:\n");
        orderDto = orderRequest.findById(orderId);
        System.out.println(orderDto);
        System.out.println("\n");


        System.out.println("Изменим заказ:\n");
        // Не засоряем канал лишними данными
        // (о времени, сервер все равно нам не доверяет и использует свои данные)
        orderDto.setCreated(null);
        orderDto.setUpdated(null);
        orderDto.getItemList().stream().forEach(oi -> {
            oi.setCreated(null);
            oi.setUpdated(null);
        });
        List<OrderItemDto> oiList = orderDto.getItemList();
        oiList.get(0).setCount(11);
        oiList.get(1).setCount(12);
        oiList.get(2).setCount(13);
        oiList.remove(3);
        orderRequest.save(orderDto);


        System.out.println("Проверим заказ:\n");
        orderDto = orderRequest.findById(orderId);
        System.out.println(orderDto);
        System.out.println("\n");


        

    }

}

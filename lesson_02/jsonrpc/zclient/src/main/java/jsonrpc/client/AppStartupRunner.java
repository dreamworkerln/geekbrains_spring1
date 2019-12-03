package jsonrpc.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import jsonrpc.client.configuration.ClientProperties;
import jsonrpc.client.request.OrderRequest;
import jsonrpc.client.request.ProductRequest;
import jsonrpc.client.request.StorageRequest;
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
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

@Component
public class AppStartupRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    private final ClientProperties clientProperties;
    private final ProductRequest productRequest;
    private final StorageRequest storageRequest;
    private final OrderRequest orderRequest;
    //private final ObjectMapper objectMapper;


    @Autowired
    public AppStartupRunner(ClientProperties clientProperties,
                            ProductRequest productRequest,
                            StorageRequest storageRequest,
                            OrderRequest orderRequest) {

        this.clientProperties = clientProperties;
        this.productRequest = productRequest;
        this.storageRequest = storageRequest;
        this.orderRequest = orderRequest;
        //this.objectMapper = objectMapper;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {


        /*

        ProductDto p = new ProductDto();
        p.setVcode("@@@@@@@@@@@");
        p.setName("ЯЯЯЯЯЯЯЯЯЯЯЯЯЯЯЯЯЯЯЯЯЯЯЯЯ");
        p.setPrice(BigDecimal.valueOf(1000));
        productRequest.save(p);


        p.setId(1L);
        p.setVcode("@@@@@@@@@@@");
        p.setName("ЙЙЙЙЙЙЙЙЙЙЙЙЙЙЙЙЙЙЙЙЙЙЙЙЙЙЙЙЙЙЙЙЙЙЙЙЙ");
        p.setPrice(BigDecimal.valueOf(1000));
        productRequest.save(p);


        p.setId(1L);
        p.setVcode("@@@@@@@@@@@");
        p.setName("ХХХХХХХХХХХХХХХХХХХХХХХХХХХХХХХХХХХХХХХХХ");
        p.setPrice(BigDecimal.valueOf(1000));
        productRequest.save(p);



        p = productRequest.findById(1L);
        System.out.println(p);
        System.out.println("\n");

        */


        /*
        try {
            System.out.println("Попытаемся забрать со склада 900 единиц товара с id=1:\n");
            storageRequest.remove(1L, 900);
        } catch (HttpStatusCodeException e) {
            log.error("HTTP " + e.getStatusCode().toString() +"\n" +
                      new String(e.getResponseBodyAsByteArray(),StandardCharsets.UTF_8.name()));
            //System.out.println("JRPC ERROR: " + objectMapper.readTree(e.getResponseBodyAsString()).get("error"));
        }
        */







/*









        System.out.println("Using client config: " + clientProperties.getServer());
        System.out.println("\n");

        System.out.println("Список товаров:\n");
        List<ProductDto> productDtoList = productRequest.findAll();
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

        */

        System.out.println("Сделаем заказ:\n");
        OrderDto orderDto = new OrderDto();
        OrderItemDto itemDto;
        itemDto  = new OrderItemDto(1L, 2);
        orderDto.addItemDto(itemDto);
        itemDto  = new OrderItemDto(2L, 15);
        orderDto.addItemDto(itemDto);
        itemDto  = new OrderItemDto(3L, 12);
        orderDto.addItemDto(itemDto);
        itemDto  = new OrderItemDto(4L, 18);
        orderDto.addItemDto(itemDto);
        Long orderId = orderRequest.save(orderDto);
        System.out.println("orderId: " + orderId);
        System.out.println("\n");

        System.out.println("Проверим заказ:\n");
        orderDto = orderRequest.findById(orderId);
        System.out.println(orderDto);
        System.out.println("\n");


        System.out.println("Изменим заказ:\n");
        orderDto.getItemList().get(0).setCount(10);
        orderDto.getItemList().get(0).setCount(9);
        orderDto.getItemList().get(0).setCount(8);
        orderDto.getItemList().get(0).setCount(7);
        orderRequest.save(orderDto);

        System.out.println("Проверим заказ:\n");
        orderDto = orderRequest.findById(orderId);
        System.out.println(orderDto);
        System.out.println("\n");



    }

}

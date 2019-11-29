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
import java.util.List;

@Component
public class AppStartupRunner implements ApplicationRunner {

    private static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    private final ClientProperties clientProperties;
    private final ProductRequest productRequest;
    private final StorageRequest storageRequest;
    private final OrderRequest orderRequest;
    private final ObjectMapper objectMapper;


    @Autowired
    public AppStartupRunner(ClientProperties clientProperties, ProductRequest productRequest, StorageRequest storageRequest, OrderRequest orderRequest, ObjectMapper objectMapper) {

        this.clientProperties = clientProperties;
        this.productRequest = productRequest;
        this.storageRequest = storageRequest;
        this.orderRequest = orderRequest;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {


        System.out.println("Using client config: " + clientProperties.getServer());
        System.out.println("\n");

        System.out.println("Список товаров:\n");
        List<ProductDto> productDtoList = productRequest.getAll();
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
            log.error("HTTP " + e.getStatusCode().toString() +"\n" + e.getResponseBodyAsString());
            //System.out.println("JRPC ERROR: " + objectMapper.readTree(e.getResponseBodyAsString()).get("error"));
        }

        System.out.println("Сделаем заказ:\n");
        Long orderId = orderRequest.put(3L, 10);
        System.out.println("orderId: " + orderId);
        System.out.println("\n");

        System.out.println("Проверим заказ:\n");
        OrderDto orderDto = orderRequest.getById(orderId);
        System.out.println(orderDto);
        System.out.println("\n");
    }

}

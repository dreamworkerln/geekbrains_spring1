package jsonrpc.client;

import jsonrpc.client.configuration.ClientProperties;
import jsonrpc.client.request.OrderRequest;
import jsonrpc.client.request.ProductRequest;
import jsonrpc.client.request.StorageRequest;
import jsonrpc.client.request.admin.AdminRequest;
import jsonrpc.client.request.base.MyToken;
import jsonrpc.client.request.base.OauthRequest;
import jsonrpc.protocol.dto.product.spec.ProductSpecDto;
import jsonrpc.protocol.dto.order.OrderDto;
import jsonrpc.protocol.dto.order.OrderItemDto;
import jsonrpc.protocol.dto.product.ProductDto;
import jsonrpc.protocol.dto.product.ProductItemDto;
import jsonrpc.utils.RestTemplateFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class AppStartupRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    private final ClientProperties clientProperties;
    private final ProductRequest productRequest;
    private final StorageRequest storageRequest;
    private final OrderRequest orderRequest;
    private final OauthRequest oauthRequest;

    private final AdminRequest adminRequest;


    @Autowired
    public AppStartupRunner(ClientProperties clientProperties,
        ProductRequest productRequest,
        StorageRequest storageRequest,
        OrderRequest orderRequest,
        OauthRequest oauthRequest, AdminRequest adminRequest) {

        this.clientProperties = clientProperties;
        this.productRequest = productRequest;
        this.storageRequest = storageRequest;
        this.orderRequest = orderRequest;
        this.oauthRequest = oauthRequest;
        this.adminRequest = adminRequest;
    }









    @Override
    public void run(ApplicationArguments args) throws Exception {







        //someTest();


        // Обнесем склад

        try {
            System.out.println("Обнесем склад с товаром id=1 -----------------------------------------------------------\n");
            storageRequest.remove(2L, 99999);
            System.out.println("\n");
        }
        catch (HttpClientErrorException.Forbidden e) {

            System.out.println(e.getMessage());
            System.out.println("\n");

            System.out.println("login as ADMIN\n");

            // перелогинимся
            clientProperties.getCredentials().setAccessToken(MyToken.EMPTY);
            clientProperties.getCredentials().setRefreshToken(MyToken.EMPTY);
            clientProperties.getCredentials().setUsername("admin");
            clientProperties.getCredentials().setPassword("password");
        }








        // OAuth2.0 INIT






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



//        System.out.println("Using client config: " + clientProperties.getResourceServer());
//        System.out.println("\n");
        
        List<ProductDto> productDtoList;


        System.out.println("Список товаров -----------------------------------------------------------------------------\n");
        productDtoList = productRequest.findAll(null);
        System.out.println(productDtoList);
        System.out.println("\n");


        System.out.println("Список товаров с ценой от 0 до 50 категории [1,2], цена ASC --------------------------------\n");
        ProductSpecDto spec = new ProductSpecDto();
        spec.getCategoryList().add(1L);
        spec.getCategoryList().add(2L);
        spec.setPriceMin(BigDecimal.valueOf(0));
        spec.setPriceMax(BigDecimal.valueOf(50));
        spec.setPriceOrderBy(ProductSpecDto.OrderBy.ASC);
        productDtoList = productRequest.findAll(spec);
        System.out.println(productDtoList);
        System.out.println("\n");


        System.out.println("Список товаров с ценой выше 30 -------------------------------------------------------------\n");
        spec = new ProductSpecDto();
        spec.setPriceMin(BigDecimal.valueOf(30));
        productDtoList = productRequest.findAll(spec);
        System.out.println(productDtoList);
        System.out.println("\n");

        System.out.println("Список всех товаров, пакетами по 3 элемента ------------------------------------------------\n");
        spec = new ProductSpecDto();
        spec.setPriceOrderBy(ProductSpecDto.OrderBy.ASC);
        spec.setLimit(3);

        BigDecimal price;
        do {
            productDtoList = productRequest.findFirst(spec);
            if (productDtoList.size() > 0) {
                System.out.println(productDtoList);
                System.out.println("\n");

                price = productDtoList.get(productDtoList.size() - 1).getPrice();
                // цены на товары скорее всего будут различаться более, чем на 0.0001
                price = price.add(new BigDecimal(0.0001)).setScale(4,BigDecimal.ROUND_HALF_UP);
                spec.setPriceMin(price);
            }
        }
        while (productDtoList.size() > 0);



        System.out.println("Запасы на складе ---------------------------------------------------------------------------\n");
        List<ProductItemDto> productItemDtoList = storageRequest.getAll();
        System.out.println(productItemDtoList);
        System.out.println("\n");


        System.out.println("Завезем на склад 500 единиц товара с id=2 --------------------------------------------------\n");
        storageRequest.put(2L, 500);
        System.out.println("\n");


        System.out.println("Запасы товара id=2 на складе ---------------------------------------------------------------\n");
        ProductItemDto productItemDto = storageRequest.getById(2L);
        System.out.println(productItemDto);
        System.out.println("\n");


        try {
            System.out.println("Попытаемся забрать со склада 9999 единиц товара с id=2 ---------------------------------\n");
            storageRequest.remove(2L, 9999);
        } catch (HttpStatusCodeException e) {
            log.error("HTTP " + e.getStatusCode().toString() +"\n" +
                      new String(e.getResponseBodyAsByteArray(),StandardCharsets.UTF_8.name()));
        }

        try {

            System.out.println("Создадим новый продукт и затем изменим его ---------------------------------------------\n");
            ProductDto productDto = new ProductDto();
            productDto.setName("Валенки");
            productDto.setCategoryId(1L);
            productDto.setPrice(BigDecimal.valueOf(10));
            productDto.setVcode("1z1z1z");
            Long productId = productRequest.save(productDto);

            productDto = productRequest.findById(productId);
            productDto.setCategoryId(2L);
            productRequest.save(productDto);

        } catch (HttpStatusCodeException e) {
            log.error("HTTP " + e.getStatusCode().toString() +"\n" +
                      new String(e.getResponseBodyAsByteArray(),StandardCharsets.UTF_8.name()));
        }



        try {

            System.out.println("Сделаем заказ --------------------------------------------------------------------------\n");
            OrderDto orderDto = new OrderDto();
            orderDto.addItem(new OrderItemDto(1L, 1));
            orderDto.addItem(new OrderItemDto(2L, 2));
            orderDto.addItem(new OrderItemDto(3L, 3));
            orderDto.addItem(new OrderItemDto(4L, 4));
            orderDto.setStatus(OrderDto.Status.ORDERED);
            Long orderId = orderRequest.save(orderDto);
            System.out.println("orderId: " + orderId);
            System.out.println("\n");




            System.out.println("Проверим заказ:\n");
            orderDto = orderRequest.findById(orderId);
            System.out.println(orderDto);
            System.out.println("\n");




            System.out.println("Изменим заказ --------------------------------------------------------------------------\n");
            // Не засоряем канал лишними данными
            // (о времени, сервер все равно нам не доверяет и использует свои данные)
            orderDto.setCreated(null);
            orderDto.setUpdated(null);
            orderDto.getItemList().forEach(oi -> {
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

            System.out.println("Продолжаем делать новые заказы, пока товар не закончится -------------------------------\n");

            while(true) {
                orderDto.setId(null);
                orderDto.getItemList().forEach(item -> item.setId(null));
                orderRequest.save(orderDto);
            }


        } catch (HttpStatusCodeException e) {
            log.error("HTTP " + e.getStatusCode().toString() +"\n" +
                      new String(e.getResponseBodyAsByteArray(),StandardCharsets.UTF_8.name()));
        }

        System.out.println("\n");
        System.out.println("Refresh token to get banned previous one ---------------------------------------------------\n");

        oauthRequest.refreshToken();

        System.out.println("\n");
        log.info("Blocked access_token on auth_server: {}", oauthRequest.getBlackList(0L));


        System.out.println("\n");
        System.out.println("Revoke all 'admin' tokens (also will kick myself) -----------------------------------------\n");

        adminRequest.revokeToken("admin");

        System.out.println("\n");
        System.out.println("Now required re-authentication or will get 403 on Bearer Authorization");

        clientProperties.getCredentials().setAccessToken(MyToken.EMPTY);
        clientProperties.getCredentials().setRefreshToken(MyToken.EMPTY);

        System.out.println("\n");
        System.out.println("Revoke all 'user' tokens -------------------------------------------------------------------\n");

        adminRequest.revokeToken("user");


    }






    private void someTest() {

        RestTemplate restTemplate = RestTemplateFactory.getRestTemplate(4000);

        String url = "https://httpstat.us/301";
        HttpHeaders headers = new HttpHeaders();
        headers.add("user-agent","gorodok 2.0");
        //headers.setContentType(MediaType.APPLICATION_JSON);

        RequestEntity requestEntity = RequestEntity
                .get(URI.create(url))
                .headers(headers)
                .build();

        log.info("REQUEST\n" + requestEntity);

        ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

        System.out.println("status: " + response.getStatusCode());
        System.out.println("body: " + response.getBody());
    }











}







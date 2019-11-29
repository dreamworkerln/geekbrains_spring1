package jsonrpc.client;

import jsonrpc.client.configuration.ClientProperties;
import jsonrpc.client.request.ProductRequest;
import jsonrpc.client.request.StorageRequest;
import jsonrpc.protocol.dto.product.ProductDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Component
public class AppStartupRunner implements ApplicationRunner {

    private static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    private final ClientProperties clientProperties;
    private final ProductRequest productRequest;
    private final StorageRequest storageRequest;


    @Autowired
    public AppStartupRunner(ClientProperties clientProperties, ProductRequest productRequest, StorageRequest storageRequest) {

        this.clientProperties = clientProperties;
        this.productRequest = productRequest;
        this.storageRequest = storageRequest;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        List<ProductDto> productDto = productRequest.getAll();
        System.out.println(productDto);

        /*

        System.out.println("Using client config: " + clientProperties.getServer());
        System.out.println("\n");

        System.out.println("Список товаров:\n");
        productRequest.getAll();
        System.out.println("\n");

        System.out.println("Запасы на складе:\n");
        storageRequest.getAll();
        System.out.println("\n");

        System.out.println("Завезем на склад 500 единиц товара с id=2:\n");
        storageRequest.put(2L, 500);
        System.out.println("\n");

        System.out.println("Запасы товара id=2 на складе:\n");
        storageRequest.getById(2L);
        System.out.println("\n");


        try {
            System.out.println("Попытаемся забрать со склада 9999 единиц товара с id=2:\n");
            storageRequest.remove(2L, 9999);
            System.out.println("\n");
        } catch (HttpStatusCodeException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getResponseBodyAsString());
        }

        */


    }

}

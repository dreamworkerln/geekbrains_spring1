package jsonrpc.server.service;


import com.github.javafaker.Faker;
import jsonrpc.server.entities.product.Product;
import jsonrpc.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormatSymbols;
import java.time.Instant;
import java.util.Locale;

@Service
public class RepositoryFakeFiller {

    //private final ConfigProperties configProperties;
    //private final ProductRepository productRepository;

    //private final StorageRepository storageRepository;

    private final ProductService productService;
    private final StorageService storageService;

    private final Faker faker = new Faker();

    @Autowired
    public RepositoryFakeFiller(ProductService productService,
                                StorageService storageService) {


        this.productService = productService;
        this.storageService = storageService;
    }


    public void fillData()  {

        //System.out.println("AppStartupRunner.run()");
        //System.out.println(configProperties.getHostName());


        // INIT PRODUCTS AND STORAGE WITH RANDOM DATA  --------------------------------

        System.out.println(faker.commerce().price());


        Product p = null;


        DecimalFormatSymbols dfs = new DecimalFormatSymbols( Locale.getDefault());
        System.out.println(Locale.getDefault());
        System.out.println( dfs.getDecimalSeparator() );

        for (long i = 0; i < 1; i++) {

            p = new Product();

            p.setName(faker.commerce().productName());
            p.setVcode(faker.number().digits(7));
            p.setPrice(BigDecimal.valueOf(Double.valueOf(faker.commerce().price())));
            //p.setPrice(BigDecimal.valueOf(67));
            p.setTestDate(Instant.EPOCH);

            productService.save(p);

            storageService.put(p, faker.number().numberBetween(1, 50));

            //p.toCreate();
            //p.toUpdate();
            //Utils.idSetter(p, i);

            //productRepository.save(p);
            //storageRepository.save(p, faker.number().numberBetween(1, 50));
        }

        storageService.put(p, 1000);

        System.out.println(storageService.findByProductId(1L).get().toString());

    }
}

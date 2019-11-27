package jsonrpc.server.service;


import com.github.javafaker.Faker;
import jsonrpc.server.configuration.ConfigProperties;
import jsonrpc.server.entities.product.Product;
import jsonrpc.server.repository.ProductRepository;
import jsonrpc.server.repository.StorageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class RepositoryFakeFiller {

    private final ConfigProperties configProperties;
    private final ProductRepository productRepository;

    private final StorageRepository storageRepository;

    private Faker faker = new Faker();

    @Autowired
    public RepositoryFakeFiller(ConfigProperties configProperties,
                                ProductRepository productRepository,
                                StorageRepository storageRepository) {

        this.configProperties = configProperties;
        this.productRepository = productRepository;
        this.storageRepository = storageRepository;
    }


    public void fillData()  {

        //System.out.println("AppStartupRunner.run()");
        //System.out.println(configProperties.getHostName());


        // INIT PRODUCTS AND STORAGE WITH RANDOM DATA  --------------------------------


        Product p;

        for (long i = 0; i < 5; i++) {

            p = new Product();

            p.setName(faker.beer().name());
            p.setVcode(faker.number().digits(7));
            p.setTestDate(Instant.EPOCH);

            p.toCreate();
            p.toUpdate();
            //Utils.idSetter(p, i);


            productRepository.put(p);
            storageRepository.put(p, faker.number().numberBetween(1, 50));
        }
    }
}

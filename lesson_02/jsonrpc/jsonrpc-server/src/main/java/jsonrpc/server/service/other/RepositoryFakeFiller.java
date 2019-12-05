package jsonrpc.server.service.other;


import com.github.javafaker.Faker;
import jsonrpc.server.entities.category.Category;
import jsonrpc.server.entities.product.Product;
import jsonrpc.server.service.CategoryService;
import jsonrpc.server.service.ProductService;
import jsonrpc.server.service.StorageService;
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
    private final CategoryService categoryService;



    private final Faker faker = new Faker();

    @Autowired
    public RepositoryFakeFiller(ProductService productService,
                                StorageService storageService, CategoryService categoryService) {


        this.productService = productService;
        this.storageService = storageService;
        this.categoryService = categoryService;
    }


    public void fillData()  {

        //System.out.println("AppStartupRunner.run()");
        //System.out.println(configProperties.getHostName());


        // INIT PRODUCTS AND STORAGE WITH RANDOM DATA  --------------------------------

        Category c = categoryService.save(new Category("очко"));

        Product p = null;
        DecimalFormatSymbols dfs = new DecimalFormatSymbols( Locale.getDefault());
        System.out.println(Locale.getDefault());
        System.out.println(dfs.getDecimalSeparator());

        for (long i = 0; i < 5; i++) {

            p = new Product();

            p.setName(faker.commerce().productName());
            p.setVcode(faker.number().digits(7));
            p.setPrice(BigDecimal.valueOf(Double.valueOf(faker.number().numberBetween(1, 50))));
            //p.setPrice(BigDecimal.valueOf(67));
            p.setTestDate(Instant.EPOCH);
            p.setCategory(c);

            productService.save(p);
            storageService.put(p, faker.number().numberBetween(1, 50));
        }

        c = categoryService.save(new Category("залупа"));
        p.setCategory(c);
        productService.save(p);
        //
        //storageService.findByProductId(1L).ifPresent(System.out::println);

    }

    public void fillDataTransactTest() {

        Category c = categoryService.save(new Category("очко"));
        Product p;
        p = new Product();

        p.setName(faker.commerce().productName());
        p.setVcode(faker.number().digits(7));
        p.setPrice(BigDecimal.valueOf(Double.valueOf(faker.number().numberBetween(1, 50))));
        p.setTestDate(Instant.EPOCH);
        p.setCategory(c);

        productService.save(p);
        storageService.put(p, 1000);
    }
}

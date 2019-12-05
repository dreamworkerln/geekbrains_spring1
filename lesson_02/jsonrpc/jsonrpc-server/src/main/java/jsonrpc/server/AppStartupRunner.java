package jsonrpc.server;

import jsonrpc.server.repository.ProductRepository;
import jsonrpc.server.service.other.RepositoryFakeFiller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AppStartupRunner implements ApplicationRunner {

    private final RepositoryFakeFiller repositoryFakeFiller;

    private final ProductRepository productRepository;

    //private final JrpcRequest jrpcRequest;



    @Autowired
    public AppStartupRunner(RepositoryFakeFiller repositoryFakeFiller, ProductRepository productRepository) {
        this.repositoryFakeFiller = repositoryFakeFiller;
        /*this.jrpcRequest = jrpcRequest;*/
        this.productRepository = productRepository;
    }


    @Override
    public void run(ApplicationArguments args) {

        repositoryFakeFiller.fillData();

        //repositoryFakeFiller.fillDataTransactTest();

        //pDto.setPriceMax(BigDecimal.valueOf(10L));

        /*
        ProductSpecification specCategory = new ProductSpecification(
                new SpecSearchCriteria("category", SearchOperation.IN, Arrays.asList(3L)));

        ProductSpecification specPrice = new ProductSpecification(
                new SpecSearchCriteria("price", SearchOperation.BETWEEN, new Long[]{1L,null}));
        */

        //System.out.println(productRepository.findAll(specCategory.and(specPrice)));

        System.out.println("213");

        System.out.println(productRepository.findAll());

    }
}
package jsonrpc.server;

import jsonrpc.protocol.dto.order.OrderDto;
import jsonrpc.server.entities.order.Order;
import jsonrpc.server.entities.order.OrderItem;
import jsonrpc.server.entities.product.Product;
import jsonrpc.server.entities.product.ProductItem;
import jsonrpc.server.repository.ProductRepository;
import jsonrpc.server.service.OrderService;
import jsonrpc.server.service.StorageService;
import jsonrpc.server.service.other.RepositoryFakeFiller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

import static jsonrpc.server.Application.SLEEP_IN_TRANSACTION;

@Component
public class AppStartupRunner implements ApplicationRunner {

    private final RepositoryFakeFiller repositoryFakeFiller;

    private final ProductRepository productRepository;
    private final OrderService orderService;
    private final StorageService storageService;

    //private final JrpcRequest jrpcRequest;



    @Autowired
    public AppStartupRunner(RepositoryFakeFiller repositoryFakeFiller, ProductRepository productRepository, OrderService orderService, StorageService storageService) {
        this.repositoryFakeFiller = repositoryFakeFiller;
        /*this.jrpcRequest = jrpcRequest;*/
        this.productRepository = productRepository;
        this.orderService = orderService;
        this.storageService = storageService;
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


        try {
            List<Product> productList = productRepository.findAll();
            System.out.println(productList);

            ProductItem productItem =  storageService.findByProductId(1L).get();

            //Product product = productItem.getProduct();


            // Тестим перекрытие транзакций с блокировкой на StorageItem

            SLEEP_IN_TRANSACTION = true;
            new Thread(
                    () -> {

                        try {
                            //Thread.sleep(1000);
                            System.out.println("Обчистим склад по 1 товару:\n");
                            storageService.remove(productItem.getProduct(), productItem.getCount());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

        }).start();



            System.out.println("Сделаем заказ:\n");
            Order order = new Order();
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(productList.get(0));
            orderItem.setCount(5);
            order.addItem(orderItem);
            order.setStatus(OrderDto.Status.QUEUED);

            Long orderId = orderService.save(order).getId();
            System.out.println("orderId: " + orderId);
            System.out.println("\n");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
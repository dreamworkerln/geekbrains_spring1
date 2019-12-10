package jsonrpc.server;

import jsonrpc.protocol.dto.order.OrderDto;
import jsonrpc.server.entities.order.Order;
import jsonrpc.server.entities.order.OrderItem;
import jsonrpc.server.entities.product.Product;
import jsonrpc.server.repository.ProductRepository;
import jsonrpc.server.service.OrderService;
import jsonrpc.server.service.other.RepositoryFakeFiller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AppStartupRunner implements ApplicationRunner {

    private final RepositoryFakeFiller repositoryFakeFiller;

    private final ProductRepository productRepository;

    private final OrderService orderService;

    //private final JrpcRequest jrpcRequest;



    @Autowired
    public AppStartupRunner(RepositoryFakeFiller repositoryFakeFiller, ProductRepository productRepository, OrderService orderService) {
        this.repositoryFakeFiller = repositoryFakeFiller;
        /*this.jrpcRequest = jrpcRequest;*/
        this.productRepository = productRepository;
        this.orderService = orderService;
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




//            System.out.println("Сделаем заказ:\n");
//            Order order = new Order();
//            OrderItem orderItem = new OrderItem();
//            orderItem.setOrder(order);
//            orderItem.setProduct(productList.get(0));
//            orderItem.setCount(5);
//            order.addItem(orderItem);
//            order.setStatus(OrderDto.Status.QUEUED);
//
//            Long orderId = orderService.save(order).getId();
//            System.out.println("orderId: " + orderId);
//            System.out.println("\n");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
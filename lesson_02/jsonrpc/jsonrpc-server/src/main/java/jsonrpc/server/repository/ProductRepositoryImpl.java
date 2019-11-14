package jsonrpc.server.repository;

import com.github.javafaker.Faker;
import jsonrpc.server.entities.product.Product;
import jsonrpc.server.utils.Utils;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Repository
//@Primary
public class ProductRepositoryImpl implements ProductRepository{

    private Faker faker = new Faker();

    @Override
    public Product getById(Long id) {


        Product result = new Product();

        // init create and update time
        result.toCreate();
        result.toUpdate();
        Utils.idSetter(result, id);


        result.setName(faker.beer().name());
        result.setvCode(faker.number().digits(7));


        result.setTestDate(Instant.EPOCH);




        return result;

    }
}

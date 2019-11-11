package jsonrpc.server.repository;

import com.github.javafaker.Faker;
import jsonrpc.server.entities.product.Product;
import jsonrpc.server.utils.Utils;
import org.springframework.stereotype.Service;

@Service
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




        return result;

    }
}

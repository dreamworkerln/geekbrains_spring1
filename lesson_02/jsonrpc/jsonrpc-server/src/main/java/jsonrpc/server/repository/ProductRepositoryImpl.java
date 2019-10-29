package jsonrpc.server.repository;

import com.github.javafaker.Faker;
import jsonrpc.server.entities.Product;
import jsonrpc.server.utils.Utils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class ProductRepositoryImpl implements ProductRepository{

    private Faker faker = new Faker();

    @Override
    public Product getById(Long id) {


        Product result = new Product();


        Utils.idSetter(result, id);

        result.setName(faker.beer().name());
        result.setvCode(faker.number().digits(7));

        return result;

    }
}

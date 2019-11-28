package jsonrpc.server.repository.impl;

import jsonrpc.server.entities.product.Product;
import jsonrpc.server.repository.ProductRepository;
import jsonrpc.utils.Utils;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
//@Primary
public class ProductRepositoryImpl implements ProductRepository {

    // In-memory DB emulation
    private ConcurrentMap<Long, Product> productList = new ConcurrentSkipListMap<>();

    private AtomicLong identity = new AtomicLong(1);

    @Override
    public void put(Product product) {

        // assign id if not persisted yet (new product)
        if (product.getId() == null) {
            Utils.idSetter(product, identity.getAndIncrement());
            product.toCreate();
        }
        product.toUpdate();
        productList.put(product.getId(), product);
    }

    @Override
    public Product getById(Long id) {

        // will return clone of product to not to end up with messed up repository data
        return Product.clone(productList.get(id));
    }


    @Override
    public List<Product> getByListId(List<Long> list) {

        List<Product> result = new ArrayList<>();

        list.forEach(i -> {

            if (productList.containsKey(i)) {
                result.add(Product.clone(productList.get(i)));

            }
        });

        return result;
    }

    @Override
    public List<Product> getAll() {
        // returning only clones!
        return productList.values().stream().map(Product::clone).collect(Collectors.toList());
    }

    // This will affect repository
    @Override
    public void delete(Long id) {

        throw new NotImplementedException("ProductRepositoryImpl.delete() not supported");
        //throw new UnsupportedOperationException("ProductRepositoryImpl.delete() not supported");

    }
}

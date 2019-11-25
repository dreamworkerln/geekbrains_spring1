package jsonrpc.server.repository.impl;

import jsonrpc.server.entities.product.Product;
import jsonrpc.server.entities.product.ProductItem;
import jsonrpc.server.repository.StorageRepository;
import jsonrpc.server.utils.Utils;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class StorageRepositoryImpl implements StorageRepository {

    // In-memory DB emulation
    // <product.id, ProductItem>
    private ConcurrentMap<Long, ProductItem> productItemList = new ConcurrentSkipListMap<>();
    private AtomicLong identity = new AtomicLong();

    @Override
    public void add(Product product, int count) {

        if (product.getId() == null) {
            throw new IllegalArgumentException("product not persisted yet");
        }

        // find ProductItem by product.id
        ProductItem tmp;

        // Increment existing ProductItem
        if(productItemList.containsKey(product.getId())) {

            tmp = productItemList.get(product.getId());
            tmp.setCount(tmp.getCount() + count);
            tmp.toUpdate();
        }
        else {
            // create new ProductItem

            tmp = new ProductItem(product, count);
            Utils.idSetter(tmp, identity.getAndIncrement());
            tmp.toCreate();
            tmp.toUpdate();
            productItemList.put(tmp.getProduct().getId(), tmp);
        }
    }

    @Override
    public void remove(Product product, int count) {

        // find ProductItem by product.id
        ProductItem tmp;

        if (product.getId() == null) {
            throw new IllegalArgumentException("product not persisted yet");
        }

        tmp = productItemList.get(product.getId());

        // Decrement existing ProductItem
        if(tmp != null && tmp.getCount() >= count) {

            tmp = productItemList.get(product.getId());
            tmp.setCount(tmp.getCount() - count);
            tmp.toUpdate();
        }
        else {
            throw new IllegalArgumentException("product doesn't exists/ not enough count in storage to remove");
        }
    }







    @Override
    public ProductItem getById(Long id) {

        return productItemList.get(id);
    }




    @Override
    public List<ProductItem> getAll() {

        return productItemList.values().stream().map(ProductItem::clone).collect(Collectors.toList());

    }

}

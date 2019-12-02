package jsonrpc.server.service.impl;

import jsonrpc.server.entities.product.Product;
import jsonrpc.server.entities.product.ProductItem;
import jsonrpc.server.entities.storage.StorageItem;
import jsonrpc.server.repository.ProductRepository;
import jsonrpc.server.repository.StorageRepository;
import jsonrpc.server.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
@Transactional
public class StorageServiceDefault implements StorageService {

    private final ProductRepository productRepository;
    private final StorageRepository storageRepository;
    private final Validator validator;


    @Autowired
    public StorageServiceDefault(ProductRepository productRepository, StorageRepository storageRepository, Validator validator) {
        this.productRepository = productRepository;
        this.storageRepository = storageRepository;
        this.validator = validator;
    }


    // -----------------------------------------------------------------------------------------------

    @Override
    public Optional<StorageItem> findById(Long id) {

         return storageRepository.findById(id);
    }

    @Override
    public List<ProductItem> findAllById(List<Long> listId) {

        // ToDO: хакатон generic bounds

        //List<SubClass> subs = ...;
        //List<? extends BaseClass> bases = subs;

        //List<BaseClass> = (List<BaseClass>) (List<? extends BaseClass>)  List<Subclass>

        // наоборот
        // List<SubClass> = (List<SubClass>)(List<?>) List<BaseClass>;


        //noinspection unchecked
        return (List<ProductItem>) (List<? extends ProductItem>) storageRepository.findAllById(listId);


    }

    @Override
    public List<ProductItem> findAll() {

        //noinspection unchecked
        return (List<ProductItem>) (List<? extends ProductItem>) storageRepository.findAll();
    }


    //https://stackoverflow.com/questions/26387399/javax-transaction-transactional-vs-org-springframework-transaction-annotation-tr
    @Override
    // ToDO: тут нужна синхронизация / транзацкия с блокировкой строки таблицы  SELECT FOR UPDATE
    // ToDo: Нужен SELECT FOR UPDATE, чтобы при отработке метода блокировалась exclusive строка с ProductItem
    @Transactional
    public void put(@NotNull Product product, int count) {

        changeCount(product, count);
    }



    // ToDO: тут нужна синхронизация / транзацкия с блокировкой строки таблицы  SELECT FOR UPDATE
    @Override
    public void remove(Product product, int count) {
        
        changeCount(product, -count);
    }




    @Override
    public void delete(@NotNull Product product) {

        storageRepository.deleteByProductId(product.getId());
    }


    @Override
    public void delete(@NotNull Long id) {

        storageRepository.deleteByProductId(id);
    }



    /**
     * служебный метод
     */
    @Override
    public Long save(ProductItem productItem) {

        storageRepository.save((StorageItem) productItem);
        return productItem.getId();
    }









    @Override
    public void validate(ProductItem product) {

        Set<ConstraintViolation<ProductItem>> violations = validator.validate(product);
        if (violations.size() != 0) {
            throw new ConstraintViolationException("ProductItem validation failed", violations);
        }
    }


    // ==================================================================================

    private void changeCount(Product product, int count) {


        if (product == null) {
            throw new IllegalArgumentException("Product == null");
        }

        if (product.getId() == null) {
            throw new IllegalArgumentException("Product.id == null");
        }

        ProductItem pi = storageRepository.findByProductId(product.getId())
                .orElseThrow(() -> new IllegalArgumentException("Product id==" + product.getId() + " not found"));

        if (pi.getCount() + count < 0) {
            throw new IllegalArgumentException("Невозможно забрать со склада " + count + " единиц товара " +
                    product.getId() + " " + ", на складе в наличии " + pi.getCount());
        }


        pi.setCount(pi.getCount() + count);
    }



}

package jsonrpc.server.service.impl;

import jsonrpc.server.entities.product.Product;
import jsonrpc.server.entities.product.ProductItem;
import jsonrpc.server.entities.storage.StorageItem;
import jsonrpc.server.repository.StorageRepository;
import jsonrpc.server.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.access.InvalidInvocationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
@Transactional
public class StorageServiceD implements StorageService {

    //private final ProductRepository productRepository;
    private final StorageRepository storageRepository;
    private final Validator validator;


    @Autowired
    public StorageServiceD(StorageRepository storageRepository, Validator validator) {
        this.storageRepository = storageRepository;
        this.validator = validator;
    }


    // -----------------------------------------------------------------------------------------------


    @Override
    public Optional<? extends ProductItem> findById(Long id) {

        return storageRepository.findById(id);
    }

    @Override
    public Optional<? extends ProductItem> findByProductId(Long id) {

         return storageRepository.findByProductId(id);
    }

    @Override
    public List<ProductItem> findAllByProductId(List<Long> listId) {


        // Cast List<SubClass> -> List<BaseClass>
        //
        // List<SubClass> subs = ...;
        // List<? extends BaseClass> bases = subs;
        //
        // List<BaseClass> = (List<BaseClass>) (List<? extends BaseClass>)  List<Subclass>
        //
        //
        // наоборот List<BaseClass> -> List<SubClass>
        //
        // List<SubClass> = (List<SubClass>)(List<?>) List<BaseClass>;

        // ToDO: хакатон generic bounds
        //noinspection unchecked
        return (List<ProductItem>) (List<? extends ProductItem>) storageRepository.findAllByProductIdList(listId);

        //return storageRepository.findAllByProductId(listId);
    }

    @Override
    public List<ProductItem> findAll() {

        //noinspection unchecked
        return (List<ProductItem>) (List<? extends ProductItem>) storageRepository.findAll();

        //return storageRepository.findAll();
    }


    @Override
    public void put(@NotNull Product product, int count) {

        changeCount(product, count);
    }




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

    @Override
    public ProductItem save(ProductItem productItem) {
        
        // перепаковываемся
        return storageRepository.save(StorageItem.from(productItem));
    }


    @Override
    public StorageItem save(StorageItem storageItem) {

        return storageRepository.save(storageItem);
    }




    // ==================================================================================

    private void changeCount(Product product, int count) {


        if (product == null) {
            throw new IllegalArgumentException("Product == null");
        }

        if (product.getId() == null) {
            throw new IllegalArgumentException("Product.id == null");
        }

        //ProductItem pi = storageRepository.findByProductIdLock(product.getId())
        ProductItem pi = storageRepository.lockByProduct(product)
                .orElseThrow(() -> new IllegalArgumentException("Product id==" + product.getId() + " not found"));


        if (pi.getCount() + count < 0) {
            throw new IllegalArgumentException("Невозможно забрать со склада " + -count + " единиц товара id==" +
                    product.getId() + ", на складе в наличии " + pi.getCount());
        }



        // ToDo: Для тестов тут спим для проверки LOCK SELECT FOR UPDATE на 1 строку с товаром:
        try {

            int l = 30;

            System.out.print("ДУМАЕМ .");
            Thread.sleep(l);
            System.out.print(".");
            Thread.sleep(l);
            System.out.println(".");
            Thread.sleep(l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        pi.setCount(pi.getCount() + count);

        storageRepository.save((StorageItem) pi);
    }



}

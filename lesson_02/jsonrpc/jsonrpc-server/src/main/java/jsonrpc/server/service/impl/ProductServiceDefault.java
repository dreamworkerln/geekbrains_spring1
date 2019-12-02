package jsonrpc.server.service.impl;

import jsonrpc.server.entities.product.Product;
import jsonrpc.server.entities.storage.StorageItem;
import jsonrpc.server.repository.ProductRepository;
import jsonrpc.server.service.ProductService;
import jsonrpc.server.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class ProductServiceDefault implements ProductService {

    private final ProductRepository productRepository;
    private final StorageService storageService;
    private final Validator validator;

    @Autowired
    public ProductServiceDefault(ProductRepository productRepository,
                                 StorageService storageService,
                                 Validator validator) {

        this.productRepository = productRepository;
        this.storageService = storageService;
        this.validator = validator;
    }


    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public List<Product> findAllById(List<Long> listId) {

        return productRepository.findAllById(listId);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Long save(Product product) {


        productRepository.save(product);

        // Инициализируем товар на складе, с product.id и count == 0
        StorageItem pi = new StorageItem(product , 0);
        storageService.save(pi);

        return product.getId();
    }


    @Override
    public void delete(Product product) {

        storageService.delete(product);
    }

    // ----------------------------------------------------------------------------
    @Override
    public void validate(Product product) {

        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        if (violations.size() != 0) {
            throw new ConstraintViolationException("Product validation failed", violations);
        }
    }

}

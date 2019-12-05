package jsonrpc.server.service;

import jsonrpc.server.entities.product.Product;
import jsonrpc.server.entities.storage.StorageItem;
import jsonrpc.server.repository.ProductRepository;
import jsonrpc.server.service.ProductService;
import jsonrpc.server.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
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
public class ProductService {

    private final ProductRepository productRepository;
    private final StorageService storageService;

    @Autowired
    public ProductService(ProductRepository productRepository,
                           StorageService storageService) {

        this.productRepository = productRepository;
        this.storageService = storageService;
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public List<Product> findAllById(List<Long> listId) {

        return productRepository.findAllById(listId);
    }

//    public List<Product> findAll() {
//        return productRepository.findAll();
//    }

    public List<Product> findAll(Specification<Product> spec) {
        return productRepository.findAll(spec);
    }

    public Product save(Product product) {

        Product result = productRepository.save(product);

        // Инициализируем товар на складе, с product.id и count == 0,
        // если такого типа продукта нет на складе
        if (!storageService.findByProductId(product.getId()).isPresent()) {
            StorageItem pi = new StorageItem(product, 0);
            storageService.save(pi);
        }

        return result;
    }


    public void delete(Product product) {

        storageService.delete(product);
    }

}
package jsonrpc.server.service;

import jsonrpc.server.entities.category.Category;
import jsonrpc.server.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    public List<Category> findAllById(List<Long> listId) {

        return categoryRepository.findAllById(listId);
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }


    public Category save(Category category) {

        return categoryRepository.save(category);
    }

    public void delete(Category category) {

        categoryRepository.delete(category);
    }
}

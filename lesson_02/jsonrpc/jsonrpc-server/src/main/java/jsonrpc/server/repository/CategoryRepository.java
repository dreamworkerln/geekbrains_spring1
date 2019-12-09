package jsonrpc.server.repository;

import jsonrpc.server.entities.category.Category;
import jsonrpc.server.repository.base.CustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends CustomRepository<Category, Long> {
}

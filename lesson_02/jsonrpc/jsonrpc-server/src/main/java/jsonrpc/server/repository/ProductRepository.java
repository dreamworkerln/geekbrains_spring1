package jsonrpc.server.repository;

import jsonrpc.server.entities.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// public interface OrderRepository extends JpaRepository<product, Long> {}
// emulation
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

//    Already exists findAllbyId()
//    @Query("from Product p " +
//           "where p.id in (:idList)")
//    List<Product> findByIdList(@Param("idList")List<Long> idList);

    



//    void save(Product product);
//    Product getById(Long id);
//    List<Product> getByIdList(List<Long> list);
//    List<Product> getAll();
//    void delete(Long id);
}

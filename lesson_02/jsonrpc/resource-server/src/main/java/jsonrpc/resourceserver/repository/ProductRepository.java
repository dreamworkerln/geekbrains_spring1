package jsonrpc.resourceserver.repository;

import jsonrpc.resourceserver.entities.product.Product;
import jsonrpc.resourceserver.repository.base.CustomRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

// public interface OrderRepository extends JpaRepository<product, Long> {}
// emulation
public interface ProductRepository extends CustomRepository<Product, Long>, JpaSpecificationExecutor<Product> {


//    Already exists findAllbyId()
//    @Query("from product p " +
//           "where p.id in (:idList)")
//    List<product> findByIdList(@Param("idList")List<Long> idList);

    



//    void save(product product);
//    product getById(Long id);
//    List<product> getByIdList(List<Long> list);
//    List<product> getAll();
//    void delete(Long id);
}

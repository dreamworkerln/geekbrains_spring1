package jsonrpc.server.repository;

import jsonrpc.server.entities.order.Order;
import jsonrpc.server.entities.order.OrderItem;
import jsonrpc.server.entities.product.Product;
import jsonrpc.server.repository.base.CustomRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface OrderRepository extends CustomRepository<Order, Long> {

    // Вешает блокировку(строка по id) на
    // Product,
    // StorageItem,
    // Order         // так же как и на Product, на всякий пожарный
    // OrderItem
    //
    // Ничего не возвращаем, делается чисто один lock, дальше можно модифицировать
    // другими запросами

    // "WHERE p.id = :#{#product.id}

    // ToDo: А нужен ли FETCH ???
//
//    @Lock(LockModeType.PESSIMISTIC_WRITE)
//    @Query("FROM OrderItem oi " +
//           "INNER JOIN FETCH oi.product p " +
//           "INNER JOIN FETCH oi.order o " +
//           "INNER JOIN FETCH StorageItem si " +
//           "WHERE oi = :#{#item} AND " +
//           "si.product IN :#{#item.product}")
//    void lockByOrderAndProduct(@Param("item")OrderItem item);


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("FROM Order o " +
           "INNER JOIN o.itemList oi " +
           "INNER JOIN oi.product " + // На всякий и product заблокируем
           "INNER JOIN StorageItem si ON oi.product = si.product " +
           "WHERE o = :#{#order}")
    List<Order> lockByOrder(@Param("order")Order order);


//    @Query(value = "UPDATE Order o SET o.updated = :timestamp " +
//            "WHERE o = :order")
//            //"WHERE o.id = :id")
//            // :#{#order.id}
//    @Modifying
//    @Query(
//            value = "UPDATE \"order\" SET updated = :timestamp " +
//                    "WHERE id = :#{#order.id}",
//            nativeQuery = true)
//    void toUpdateNative(@Param("order") Order order, @Param("timestamp")Instant timestamp);
//
//
//    @Modifying
//        @Query(value = "UPDATE Order o SET o.updated = CURRENT_TIMESTAMP " +
//            "WHERE o = :order")
//            //"WHERE o.id = :id")
//            // :#{#order.id}
//
//    void toUpdate(@Param("order") Order order);
}

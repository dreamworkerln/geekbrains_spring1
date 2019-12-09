package jsonrpc.server.repository;

import jsonrpc.server.entities.order.Order;
import jsonrpc.server.repository.base.CustomRepository;

public interface OrderRepository extends CustomRepository<Order, Long> {


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

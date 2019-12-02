package jsonrpc.server.repository;

import jsonrpc.server.entities.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Modifying
    @Query(value = "UPDATE Order o SET o.updated = :timestamp " +
            "WHERE o = :order")
            //"WHERE o.id = :id")
            // :#{#order.id}
    void toUpdate(@Param("order") Order order, @Param("timestamp")Instant timestamp);
}

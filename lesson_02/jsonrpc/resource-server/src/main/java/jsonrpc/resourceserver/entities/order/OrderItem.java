package jsonrpc.resourceserver.entities.order;

import jsonrpc.resourceserver.entities.product.ProductItem;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity
@Table(name="orderzItem")
public class OrderItem extends ProductItem {

    @NotNull
    @ManyToOne
    @JoinColumn(name="order_id", referencedColumnName="id")
    private Order order;

    public Order getOrder() {return order;}

    public void setOrder(Order order) {this.order = order;}

    @Override
    public String toString() {
        return "OrderItem{" +
               "order=" + order.getId() +
               ", product=" + product +
               ", count=" + count +
               ", id=" + id +
               '}';
    }
}

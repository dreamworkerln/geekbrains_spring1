package jsonrpc.server.entities.order;

import jsonrpc.server.entities.product.ProductItem;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name="orderItem")
public class OrderItem extends ProductItem {


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

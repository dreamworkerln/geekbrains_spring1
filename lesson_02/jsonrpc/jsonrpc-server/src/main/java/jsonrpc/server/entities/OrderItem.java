package jsonrpc.server.entities;

import javax.persistence.*;

@Entity
@Table(name="orderItem")
public class OrderItem {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name="product_id", referencedColumnName="id")
    private Product product;

    private Integer count;

    @ManyToOne
    @JoinColumn(name="order_id", referencedColumnName="id")
    private Order order;

    public OrderItem() {}


    public OrderItem(Product product, Integer count) {
        this.product = product;
        this.count = count;
    }

    protected void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}

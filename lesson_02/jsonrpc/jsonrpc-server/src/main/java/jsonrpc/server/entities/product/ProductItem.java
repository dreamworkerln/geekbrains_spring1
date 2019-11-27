package jsonrpc.server.entities.product;

import jsonrpc.server.entities.base.AbstractEntityPersisted;
import org.apache.commons.lang3.SerializationUtils;

import javax.persistence.*;


/*
Содержит информацию о количестве товара данного типа
Используется в Storage(хранилище) и Order(корзина)
*/

//@MappedSuperclass
@MappedSuperclass
public class ProductItem extends AbstractEntityPersisted {

    @ManyToOne
    @JoinColumn(name="product_id", referencedColumnName="id")
    protected Product product;

    protected Integer count;

    //@ManyToOne
    //@JoinColumn(name="order_id", referencedColumnName="id")
    //private Order order;

    public ProductItem() {}

    public ProductItem(Product product, Integer count) {
        this.product = product;
        this.count = count;
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


    public ProductItem clone() {

        return SerializationUtils.clone(this);
    }

//    public Order getOrder() {
//        return order;
//    }
//
//    public void setOrder(Order order) {
//        this.order = order;
//    }


    @Override
    public String toString() {
        return "ProductItem{" +
               "product=" + product +
               ", count=" + count +
               '}';
    }
}

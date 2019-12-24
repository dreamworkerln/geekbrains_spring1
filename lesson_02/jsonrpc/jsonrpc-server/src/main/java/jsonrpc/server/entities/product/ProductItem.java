package jsonrpc.server.entities.product;

import jsonrpc.server.entities.base.AbstractEntity;
import org.apache.commons.lang3.SerializationUtils;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


/*
Содержит информацию о количестве товара данного типа
Используется в StorageN(хранилище) и OrderN(корзина)
*/

//@MappedSuperclass
@MappedSuperclass
public class ProductItem extends AbstractEntity {

    @NotNull
    @ManyToOne//@OneToOne //@ManyToOne
    @JoinColumn(name="product_id", referencedColumnName="id")
    protected Product product;

    @NotNull
    @Min(0)
    protected Integer count;

    //@ManyToOne
    //@JoinColumn(name="order_id", referencedColumnName="id")
    //private OrderN order;

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


    public static ProductItem clone(ProductItem productItem) {

        ProductItem result = null;

        if (productItem != null) {
            result = SerializationUtils.clone(productItem);
        }
        return result;
    }

//    public OrderN getOrder() {
//        return order;
//    }
//
//    public void setOrder(OrderN order) {
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

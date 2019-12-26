package jsonrpc.resourceserver.entities.storage;



import jsonrpc.resourceserver.entities.product.Product;
import jsonrpc.resourceserver.entities.product.ProductItem;

import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * Represents available goodies in shop
 */
@Entity
@Table(name="storageItem")
public class StorageItem extends ProductItem {

    public StorageItem() {}

    public StorageItem(Product product, Integer count) {
        this.product = product;
        this.count = count;
    }

    public static StorageItem from(ProductItem productItem) {
        return new StorageItem(productItem.getProduct(), productItem.getCount());
    }


}

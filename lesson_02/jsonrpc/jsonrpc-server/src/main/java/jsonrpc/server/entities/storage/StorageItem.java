package jsonrpc.server.entities.storage;



import jsonrpc.server.entities.product.Product;
import jsonrpc.server.entities.product.ProductItem;

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


}

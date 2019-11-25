package jsonrpc.server.entities.storage;



import jsonrpc.server.entities.product.ProductItem;

import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * represents available goodies in shop
 */
@Entity
@Table(name="storage")
public class Storage extends ProductItem {

}

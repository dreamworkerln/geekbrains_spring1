package jsonrpc.server.entities.order;

import jsonrpc.server.entities.Client;
import jsonrpc.server.entities.Manager;
import jsonrpc.server.entities.base.AbstractEntityPersisted;
import org.apache.commons.lang3.SerializationUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


// Заказ пользователя
@Entity
@Table(name="\"order\"")  // Ambiguous with hsql keyword 'order'
public class Order extends AbstractEntityPersisted {

    @NotNull
    @OneToMany(mappedBy= "order", cascade = CascadeType.ALL)
    @OrderBy("id ASC")
    private List<OrderItem> itemList = new ArrayList<>();


    @ManyToOne
    @JoinColumn(name="client_id", referencedColumnName="id")
    private Client client;

    @ManyToOne
    @JoinColumn(name="manager_id", referencedColumnName="id")
    private Manager manager;


//    public Long getId() {
//        return id;
//    }

    public void addItem(OrderItem item) {
        itemList.add(item);
        item.setOrder(this);
    }

//    public void addList(Collection<OrderItem> list) {
//        list.forEach(item -> {
//            itemList.add(item);
//            item.setOrder(this);
//        });

    public List<OrderItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<OrderItem> itemList) {
        this.itemList = itemList;
        itemList.forEach(item -> item.setOrder(this));
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }


    public static Order clone(Order order) {

        Order result = null;

        if (order != null) {
            result = SerializationUtils.clone(order);
        }
        return result;
    }


    @Override
    public String toString() {
        return "Order{" +
               "itemList=" + itemList +
               ", client=" + client +
               ", manager=" + manager +
               '}';
    }
}

package jsonrpc.server.entities;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name="orders")  // Ambiguous with hsql keyword 'order'
public class Order {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy= "order", fetch=FetchType.LAZY)
    @OrderBy("id ASC")
    private List<OrderItem> itemList = new ArrayList<>();


    @ManyToOne
    @JoinColumn(name="client_id", referencedColumnName="id")
    private Client client;

    @ManyToOne
    @JoinColumn(name="manager_id", referencedColumnName="id")
    private Manager manager;

    private Instant date;

    protected void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void addItem(OrderItem item) {
        itemList.add(item);
        item.setOrder(this);
    }

//    public void addList(Collection<OrderItem> list) {
//        list.forEach(item -> {
//            itemList.add(item);
//            item.setOrder(this);
//        });
//    }

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

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

}

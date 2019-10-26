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

    // ToDO: remove setId setter after ending OrderRepositoryImpl emulation
    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void addItem(OrderItem item) {
        itemList.add(item);
        item.setOrder(this);
    }

    public void addList(Collection<OrderItem> list) {
        list.forEach(item -> {
            itemList.add(item);
            item.setOrder(this);
        });
    }

    public List<OrderItem> getItems() {
        return itemList;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }
}

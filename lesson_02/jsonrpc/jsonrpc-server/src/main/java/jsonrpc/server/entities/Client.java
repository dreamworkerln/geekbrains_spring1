package jsonrpc.server.entities;

import jsonrpc.server.entities.base.Person;
import jsonrpc.server.entities.order.Order;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="client")
public class Client extends Person {

    @OneToMany(mappedBy= "client", fetch=FetchType.LAZY)
    @OrderBy("id ASC")
    private List<Order> orderList = new ArrayList<>();

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }
}

package jsonrpc.server.entities;

import jsonrpc.server.entities.base.Person;
import jsonrpc.server.entities.order.Order;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="manager")
public class Manager extends Person {

    @OneToMany(mappedBy= "manager", fetch=FetchType.LAZY)
    @OrderBy("id ASC")
    private List<Order> orderList = new ArrayList<>();
}

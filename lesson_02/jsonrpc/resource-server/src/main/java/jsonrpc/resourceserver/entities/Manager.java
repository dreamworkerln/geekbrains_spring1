package jsonrpc.resourceserver.entities;

import jsonrpc.resourceserver.entities.base.Person;
import jsonrpc.resourceserver.entities.order.Order;

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

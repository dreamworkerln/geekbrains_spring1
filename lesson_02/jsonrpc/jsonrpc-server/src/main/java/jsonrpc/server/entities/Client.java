package jsonrpc.server.entities;

import jsonrpc.server.entities.base.Person;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="client")
public class Client extends Person {

    @OneToMany(mappedBy= "client", fetch=FetchType.LAZY)
    @OrderBy("id ASC")
    private List<Order> orders = new ArrayList<>();
}

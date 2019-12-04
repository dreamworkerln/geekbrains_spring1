package jsonrpc.server.entities.category;

import jsonrpc.server.entities.base.AbstractEntityPersisted;
import jsonrpc.server.entities.order.OrderItem;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


@Entity
@Data
public class Category extends AbstractEntityPersisted {

    @NotNull
    private String name;

    @NotNull
    @OneToMany(mappedBy= "product", cascade = CascadeType.ALL)
    @OrderBy("id ASC")
    private List<OrderItem> productList = new ArrayList<>();


    public Category() {}


    public Category(String name) {
        this.name = name;
    }

    //@ManyToMany
    //private List<Product> productList = new ArrayList<>();
}

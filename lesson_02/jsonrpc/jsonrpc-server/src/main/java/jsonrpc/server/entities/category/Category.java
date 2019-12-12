package jsonrpc.server.entities.category;

import jsonrpc.server.entities.base.AbstractEntity;
import jsonrpc.server.entities.order.OrderItem;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
public class Category extends AbstractEntity {

    @NotNull
    private String name;

//    @NotNull
//    @OneToMany(mappedBy= "product")
//    @OrderBy("id ASC")
//    private List<OrderItem> productList = new ArrayList<>();

//    //@Builder
//    public Category(@NotNull String name, @NotNull List<OrderItem> productList) {
//        this.name = name;
//        this.productList = productList;
//    }

    public Category(String name) {
        this.name = name;
    }

    //@ManyToMany
    //private List<ProductN> productList = new ArrayList<>();
}

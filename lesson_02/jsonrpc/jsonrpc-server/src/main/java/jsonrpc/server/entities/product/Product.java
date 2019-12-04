package jsonrpc.server.entities.product;

import jsonrpc.server.entities.base.AbstractEntityPersisted;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;

import jsonrpc.server.entities.category.Category;
import org.apache.commons.lang3.SerializationUtils;

@Entity
@Table(name="product")
public class Product extends AbstractEntityPersisted {

    @NotNull
    private String name;

    @NotNull
    private String vcode; // Артикул

    @NotNull
    private BigDecimal price;

    @NotNull
    @ManyToOne
    @JoinColumn(name="category_id", referencedColumnName="id")
    Category category;

    //private List<Category> categoryList = new ArrayList<>();


    public String getName() {return name;}

    public void setName(String name) {
        this.name = name;
    }

    public String getVcode() {
        return vcode;
    }

    public void setVcode(String vcode) {
        this.vcode = vcode;
    }

    public BigDecimal getPrice() {return price;}

    public void setPrice(BigDecimal price) {this.price = price;}

    // ----------------------------------------------- TEST --------------------------------------

    private Instant testDate;

    public Instant getTestDate() {
        return testDate;
    }

    public void setTestDate(Instant testDate) {
        this.testDate = testDate;
    }

    public static Product clone(Product product) {

        Product result = null;

        if (product != null) {
            result = SerializationUtils.clone(product);
        }
        return result;
    }

    public Category getCategory() {return category;}

    public void setCategory(Category category) {this.category = category;}

    @Override
    public String toString() {
        return "product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", vcode='" + vcode + '\'' +
                ", price=" + price +
                ", testDate=" + testDate +
                ", created=" + created +
                ", updated=" + updated +
                '}';
    }
}

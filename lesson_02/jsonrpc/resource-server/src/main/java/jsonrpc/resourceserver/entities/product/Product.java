package jsonrpc.resourceserver.entities.product;

import jsonrpc.resourceserver.entities.base.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;

import jsonrpc.resourceserver.entities.category.Category;
import org.apache.commons.lang3.SerializationUtils;

@Entity
@Table(name="product")
public class Product extends AbstractEntity {

    @NotNull
    private String name;

    @NotNull
    private String vcode; // Артикул

    @NotNull
    private BigDecimal price;

    @NotNull
    @ManyToOne
    @JoinColumn(name="category_id", referencedColumnName="id")
    private
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

    public Category getCategory() {return category;}

    public void setCategory(Category category) {this.category = category;}

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




    @Override
    public String toString() {
        return "product{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", vcode='" + vcode + '\'' +
               ", price=" + price +
               ", category=" + (category != null ? Long.toString(category.getId()) : null) +
               ", testDate=" + testDate +
               ", created=" + created +
               ", updated=" + updated +
               '}';
    }




}

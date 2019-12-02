package jsonrpc.server.entities.product;

import jsonrpc.server.entities.base.AbstractEntityPersisted;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;

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


    @Override
    public String toString() {
        return "Product{" +
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

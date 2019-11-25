package jsonrpc.server.entities.product;

import jsonrpc.server.entities.base.AbstractEntityPersisted;

import javax.persistence.*;
import java.time.Instant;
import org.apache.commons.lang3.SerializationUtils;

@Entity
@Table(name="product")
public class Product extends AbstractEntityPersisted {

    private String name;
    private String vcode; // Артикул



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

    // ----------------------------------------------- TEST --------------------------------------

    private Instant testDate;

    public Instant getTestDate() {
        return testDate;
    }

    public void setTestDate(Instant testDate) {
        this.testDate = testDate;
    }

    public Product clone() {

        return SerializationUtils.clone(this);
    }


    @Override
    public String toString() {
        return "product{" +
               "name='" + name + '\'' +
               '}';
    }
}

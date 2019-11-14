package jsonrpc.server.entities.product;

import jsonrpc.server.entities.base.AbstractEntityPersisted;
import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name="product")
public class Product extends AbstractEntityPersisted {

    private String name;
    private String vCode;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getvCode() {
        return vCode;
    }

    public void setvCode(String vCode) {
        this.vCode = vCode;
    }

    // ----------------------------------------------- TEST --------------------------------------

    private Instant testDate;

    public Instant getTestDate() {
        return testDate;
    }

    public void setTestDate(Instant testDate) {
        this.testDate = testDate;
    }
}

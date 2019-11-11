package jsonrpc.server.entities.product;

import jsonrpc.server.entities.base.AbstractEntity;

import javax.persistence.*;

@Entity
@Table(name="product")
public class Product extends AbstractEntity {

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
}

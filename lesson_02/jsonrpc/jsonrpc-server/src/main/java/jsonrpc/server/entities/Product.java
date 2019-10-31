package jsonrpc.server.entities;

import jsonrpc.server.entities.base.AbstractEntity;

import javax.persistence.*;

@Entity
@Table(name="product")
public class Product extends AbstractEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String vCode;

    protected void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

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

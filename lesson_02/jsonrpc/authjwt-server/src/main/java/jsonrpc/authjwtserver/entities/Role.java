package jsonrpc.authjwtserver.entities;

import jsonrpc.authjwtserver.entities.base.AbstractEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="rolez")
public class Role extends AbstractEntity {

    public static final String REFRESH = "REFRESH";
    public static final String ANONYMOUS = "ANONYMOUS";
    public static final String USER = "USER";
    public static final String ADMIN = "ADMIN";

    @Column(unique=true)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();


    public Role() {}

    public Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getUsers() {
        return users;
    }
}

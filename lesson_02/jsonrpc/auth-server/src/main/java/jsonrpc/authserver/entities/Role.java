package jsonrpc.authserver.entities;

import jsonrpc.authserver.entities.base.AbstractEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="rolez")
public class Role extends AbstractEntity {

//    public static final String REFRESH   = "ROLE_REFRESH";
    public static final String ANONYMOUS = "ROLE_ANONYMOUS";
    public static final String USER      = "ROLE_USER";
    public static final String ADMIN     = "ROLE_ADMIN";

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

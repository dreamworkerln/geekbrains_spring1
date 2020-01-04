package jsonrpc.authjwtserver.entities;

import jsonrpc.authjwtserver.entities.base.AbstractEntity;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Table(name="userz")
public class User extends AbstractEntity {

    @Column(unique=true)
    private String name;

    private String password;

    // Это список только refresh_token
    @NotNull
    @OneToMany(mappedBy= "user", orphanRemoval = true, cascade = CascadeType.ALL)
    @OrderBy("id ASC")
    private List<Token> refreshTokens = new ArrayList<>();


    @ManyToMany(cascade = CascadeType.MERGE)
//    @JoinTable(
//            name = "user_role",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();





    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public List<Token> getRefreshTokens() {
        return refreshTokens;
    }

    public void setRolesEx(Collection<? extends GrantedAuthority> authorities) {

        authorities.forEach(o -> roles.add(new Role(o.getAuthority())));
    }
}

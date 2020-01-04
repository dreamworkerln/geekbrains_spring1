package jsonrpc.authserver.entities.base;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@MappedSuperclass
public abstract class AbstractEntity implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "created", updatable = false)
    @CreationTimestamp
    protected Instant created;


    @Column(name = "updated")
    @UpdateTimestamp
    protected Instant updated;


    protected Boolean enabled = true;


    protected AbstractEntity() {}

    public Long getId() {
        return id;
    }

    //@Column(name = "created", updatable = false)
    public Instant getCreated() {
        return created;
    }

    //@Column(name = "updated", insertable = false)
    public Instant getUpdated() {
        return updated;
    }

    public Boolean isEnabled() {return enabled;}
    public void setEnabled(Boolean enabled) {this.enabled = enabled;}
}





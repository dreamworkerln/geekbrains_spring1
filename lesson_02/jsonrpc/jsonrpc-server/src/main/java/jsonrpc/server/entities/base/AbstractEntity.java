package jsonrpc.server.entities.base;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

    protected Instant created;

    protected Instant updated;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    protected Long id;

    public Long getId() {
        return id;
    }

    @Column(name = "created", updatable = false)
    public Instant getCreated() {
        return created;
    }

    @Column(name = "updated", insertable = false)
    public Instant getUpdated() {
        return updated;
    }

    @PrePersist
    public void toCreate() {
        created = Instant.now();
    }

    @PreUpdate
    public void toUpdate() {
        updated = Instant.now();
    }


}

package jsonrpc.server.entities.base;

import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@MappedSuperclass
public abstract class AbstractEntityPersisted extends AbstractEntity {

    private Instant created;

    private Instant updated;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    protected Long id;

    public Long getId() {
        return id;
    }
    protected void setId(Long id) {
        this.id = id;
    }

    @Column(name = "created", updatable = false)
    public Instant getCreated() {
        return created;
    }

    @Column(name = "updated", insertable = false)
    public Instant getUpdated() {
        return updated;
    }

    @Transient
    public void setCreated(Instant created) {
        this.created = created;
    }

    @Transient
    public void setUpdated(Instant updated) {
        this.updated = updated;
    }

    @PrePersist
    public void toCreate() {
        // Truncating to seconds
        created = Instant.now().truncatedTo(ChronoUnit.SECONDS);
    }

    @PreUpdate
    public void toUpdate() {
        // Truncating to seconds
        updated = Instant.now().truncatedTo(ChronoUnit.SECONDS);
    }
}

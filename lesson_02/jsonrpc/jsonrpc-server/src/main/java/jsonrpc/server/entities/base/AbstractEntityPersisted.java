package jsonrpc.server.entities.base;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import javax.persistence.*;
import java.time.Instant;

@MappedSuperclass
public abstract class AbstractEntityPersisted extends AbstractEntity {

    @Column(name = "created", updatable = false)
    @CreationTimestamp
    protected Instant created;


    @Column(name = "updated")
    @UpdateTimestamp
    protected Instant updated;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    protected AbstractEntityPersisted(){}

    public Long getId() {
        return id;
    }
//    protected void setId(Long id) {
//        this.id = id;
//    }


    //@Column(name = "created", updatable = false)
    public Instant getCreated() {
        return created;
    }

    //@Column(name = "updated", insertable = false)
    public Instant getUpdated() {
        return updated;
    }


//    @PrePersist
//    protected void prePersist() {
//        System.out.println("prePersist");
//        created = Instant.now();
//        updated = Instant.now();
//    }
//
//    @PreUpdate
//    protected void preUpdate() {
//        System.out.println("preUpdate");
//        updated = Instant.now();
//    }
//
//    @PostUpdate
//    protected void postUpdate() {
//        System.out.println("postUpdate");
//    }
}

//    @Transient
//    public void setCreated(Instant created) {
//        this.created = created;
//    }
//
//    @Transient
//    public void setUpdated(Instant updated) {
//        this.updated = updated;
//    }

//    // Truncating to seconds
//    @PrePersist
//    public void toCreate() {
//        created = Instant.now().truncatedTo(ChronoUnit.SECONDS);
//    }
//
//    // Truncating to seconds
//    @PreUpdate
//    public void toUpdate() {
//        updated = Instant.now().truncatedTo(ChronoUnit.SECONDS);
//    }



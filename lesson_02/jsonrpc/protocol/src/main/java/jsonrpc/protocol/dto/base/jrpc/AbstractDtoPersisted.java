package jsonrpc.protocol.dto.base.jrpc;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public abstract class AbstractDtoPersisted extends AbstractDto {

    protected Long id;
    protected Long created;
    protected Long updated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public Long getUpdated() {
        return updated;
    }

    public void setUpdated(Long updated) {
        this.updated = updated;
    }

    public void toCreate() {
        // Truncating to seconds
        created = Instant.now().truncatedTo(ChronoUnit.SECONDS).getEpochSecond();
    }

    public void toUpdate() {
        // Truncating to seconds
        updated = Instant.now().truncatedTo(ChronoUnit.SECONDS).getEpochSecond();
    }

    /**
     * Set id, refresh created and updated
     * @param id
     */
    public void reCreateWithId(Long id) {

        this.id = id;
        this.toCreate();
        this.toUpdate();
    }
}


/*

import com.fasterxml.jackson.annotation.JsonFormat;

public abstract class AbstractDto implements Serializable {

    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    LocalDateTime created;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    LocalDateTime updated;
}
*/

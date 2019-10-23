package jsonrpc.protocol.dto.base;


/**
 * Base class for
 */
//@JsonIgnoreProperties(ignoreUnknown = true)
public class Message {
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

package jsonrpc.protocol.jrpc;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Base class for
 */
@Data
public abstract class JrpcBase {

    @JsonProperty("jsonrpc")
    protected String version = "2.0";

    protected Long id;
}


//@JsonIgnoreProperties(ignoreUnknown = true)

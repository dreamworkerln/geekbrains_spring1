package jsonrpc.server.entities.base;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@MappedSuperclass
public abstract class AbstractEntity implements Serializable{}

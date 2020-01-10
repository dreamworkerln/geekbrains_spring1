package jsonrpc.authserver.entities.token;

import jsonrpc.authserver.entities.User;
import jsonrpc.authserver.entities.base.AbstractEntity;
import jsonrpc.protocol.http.TokenType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@MappedSuperclass
public abstract class Token extends AbstractEntity {

    @Column(name = "expired_at", updatable = false)
    protected Instant expiredAt;

//    @Enumerated(EnumType.STRING)
//    private TokenType type;

    @NotNull
    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName="id")
    protected User user;

    protected Token() {}

    public Token(User user, boolean enabled, Instant expiredAt) {
        this.user = user;
        this.enabled = enabled;
        this.expiredAt = expiredAt;
    }

    public Instant getExpiredAt() {
        return expiredAt;
    }

    public User getUser() {return user;}
}

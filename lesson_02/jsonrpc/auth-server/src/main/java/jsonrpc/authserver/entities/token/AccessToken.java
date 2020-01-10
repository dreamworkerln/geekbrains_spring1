package jsonrpc.authserver.entities.token;

import jsonrpc.authserver.entities.User;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(indexes = {
    @Index(name = "idx_created", columnList = "created"),
    @Index(name = "idx_expired_at", columnList = "expired_at")})
public class AccessToken extends Token {

    protected AccessToken() {}

    public AccessToken(User user, boolean enabled, Instant expiredAt) {
        super(user, enabled, expiredAt);
    }
}

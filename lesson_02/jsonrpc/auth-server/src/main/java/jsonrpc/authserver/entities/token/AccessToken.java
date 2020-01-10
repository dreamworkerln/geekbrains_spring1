package jsonrpc.authserver.entities.token;

import jsonrpc.authserver.entities.User;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(indexes = {
    @Index(name = "idx_access_token_created", columnList = "created"),
    @Index(name = "idx_access_token_expired_at", columnList = "expired_at")})
public class AccessToken extends Token {

    protected AccessToken() {}

    @OneToOne
    @JoinColumn(name = "refresh_token_id", referencedColumnName = "id")
    private RefreshToken refreshToken;

    public AccessToken(User user, boolean enabled, RefreshToken refreshToken, Instant expiredAt) {
        super(user, enabled, expiredAt);
        this.refreshToken = refreshToken;
    }

    public RefreshToken getRefreshToken() {
        return refreshToken;
    }
}

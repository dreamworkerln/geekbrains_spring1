package jsonrpc.authserver.entities.token;

import jsonrpc.authserver.entities.User;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(indexes = {
    @Index(name = "idx_created", columnList = "created"),
    @Index(name = "idx_expired_at", columnList = "expired_at")})
public class RefreshToken extends Token {

    // link from refresh token to access token
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "access_token_id", referencedColumnName = "id")
    private AccessToken accessToken;

    protected RefreshToken() {}

    public RefreshToken(User user, AccessToken accessToken, boolean enabled, Instant expiredAt) {
        super(user, enabled, expiredAt);
        this.accessToken = accessToken;
    }

    public AccessToken getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
    }
}

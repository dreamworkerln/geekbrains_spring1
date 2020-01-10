package jsonrpc.authserver.entities.token;

import jsonrpc.authserver.entities.User;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(indexes = {
    @Index(name = "idx_refresh_token_created", columnList = "created"),
    @Index(name = "idx_refresh_token_expired_at", columnList = "expired_at")})
public class RefreshToken extends Token {

    // link from refresh token to access token
    @OneToOne(mappedBy = "refreshToken", cascade = CascadeType.ALL, orphanRemoval = true)
    private AccessToken accessToken;

    protected RefreshToken() {}

    public RefreshToken(User user, boolean enabled, Instant expiredAt) {
        super(user, enabled, expiredAt);
    }

    public AccessToken getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
    }
}

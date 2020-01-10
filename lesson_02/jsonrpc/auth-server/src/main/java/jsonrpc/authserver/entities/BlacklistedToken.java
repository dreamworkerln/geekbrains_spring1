package jsonrpc.authserver.entities;

import jsonrpc.authserver.entities.base.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.time.Instant;

// Blacklisted access_token
@Entity
@Table(indexes = {
        @Index(name = "idx_blacklisted_created", columnList = "created"),
        @Index(name = "idx_blacklisted_expired_at", columnList = "expired_at")})
public class BlacklistedToken extends AbstractEntity {

    @Column(unique=true)
    private Long tokenId;

    @Column(name = "expired_at", updatable = false)
    private Instant expiredAt;

    protected BlacklistedToken() {}

    public BlacklistedToken(Long tokenId, Instant expiredAt) {
        this.tokenId = tokenId;
        this.expiredAt = expiredAt;
    }

    public Long getTokenId() {
        return tokenId;
    }

    public void setTokenId(Long tokenId) {
        this.tokenId = tokenId;
    }

    public Instant getExpiredAt() {
        return expiredAt;
    }
}

package jsonrpc.authserver.entities;

import jsonrpc.authserver.entities.base.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

// Blacklisted access_token
@Entity
public class BlacklistedToken extends AbstractEntity {

    @Column(unique=true)
    private Long tokenId;

    protected BlacklistedToken() {}

    public BlacklistedToken(Long tokenId) {
        this.tokenId = tokenId;
    }

    public Long getTokenId() {
        return tokenId;
    }

    public void setTokenId(Long tokenId) {
        this.tokenId = tokenId;
    }

}

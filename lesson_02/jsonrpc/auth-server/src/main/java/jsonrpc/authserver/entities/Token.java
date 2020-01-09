package jsonrpc.authserver.entities;

import jsonrpc.authserver.entities.base.AbstractEntity;
import jsonrpc.protocol.http.TokenType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(indexes = { @Index(name = "idx_created", columnList = "created")})
public class Token extends AbstractEntity {

    // link from refresh token to access token
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "access_token_id", referencedColumnName = "id")
    private Token accessToken;

    @Enumerated(EnumType.STRING)
    private TokenType type;

    @NotNull
    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName="id")
    private User user;

    protected Token() {}

    public Token(TokenType type, User user, boolean enabled) {
        this.type = type;
        this.user = user;
        this.enabled = enabled;
    }

    public Token(TokenType type, User user, Token accessToken, boolean enabled) {
        this.type = type;
        this.user = user;
        this.accessToken = accessToken;
        this.enabled = enabled;
    }

    public Token getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(Token accessToken) {
        this.accessToken = accessToken;
    }

    public TokenType getType() {return type;}

    public User getUser() {return user;}
}

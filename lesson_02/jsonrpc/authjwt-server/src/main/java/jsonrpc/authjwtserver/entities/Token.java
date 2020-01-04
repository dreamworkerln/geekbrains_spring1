package jsonrpc.authjwtserver.entities;

import jsonrpc.authjwtserver.entities.base.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Token extends AbstractEntity {

    private Boolean approved;

    // link from refresh token to access token
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "access_token_id", referencedColumnName = "id")
    private Token accessToken;


    @NotNull
    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName="id")
    private User user;

    public Token() {}

    public Token(User user, Boolean approved) {
        this.user = user;
        this.approved = approved;
    }

    public Token(User user, Boolean approved, Token accessToken) {
        this.user = user;
        this.approved = approved;
        this.accessToken = accessToken;
    }

    public Boolean getApproved() {return approved;}
    public void setApproved(Boolean approved) {this.approved = approved;}

    public Token getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(Token accessToken) {
        this.accessToken = accessToken;
    }
}

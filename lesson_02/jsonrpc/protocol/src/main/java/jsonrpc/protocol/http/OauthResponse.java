package jsonrpc.protocol.http;

import lombok.Data;

@Data
public class OauthResponse {

    private String access_token;
    private String token_type;
    private String refresh_token;
    private Integer expires_in;
    private String[] scope;

    public void setScope(String scope) {

        this.scope = scope.split(" ");
    }
}

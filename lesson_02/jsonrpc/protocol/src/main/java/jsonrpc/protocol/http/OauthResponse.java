package jsonrpc.protocol.http;

import lombok.Data;

@Data
public class OauthResponse {


    private String accessToken;
    private String refreshToken;

    protected OauthResponse() {}

    public OauthResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }



    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}

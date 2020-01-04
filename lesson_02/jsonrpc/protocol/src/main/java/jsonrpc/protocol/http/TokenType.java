package jsonrpc.protocol.http;

public enum TokenType {



    ACCESS("access_token", 3600),
    REFRESH("refresh_token", 3600*24*30);

    private long ttl;
    private String value;


    TokenType(String value, long ttl) {
        this.ttl = ttl;
        this.value = value;
    }

    public long getTtl() {
        return ttl;
    }

    public String getValue() {
        return value;
    }
}

package jsonrpc.protocol.http;

public enum TokenType {



    ACCESS("access_token", 3600),
    REFRESH("refresh_token", 3600*24*30);

    private long ttl;
    private String name;


    TokenType(String name, long ttl) {
        this.ttl = ttl;
        this.name = name;
    }

    /**
     * In seconds
     */
    public long getTtl() {
        return ttl;
    }

    public String getName() {
        return name;
    }
}

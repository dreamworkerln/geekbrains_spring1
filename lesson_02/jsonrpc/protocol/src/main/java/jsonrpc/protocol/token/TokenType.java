package jsonrpc.protocol.token;

import java.util.HashMap;
import java.util.Map;

public enum TokenType {



    ACCESS("access_token", 3600),
    REFRESH("refresh_token", 3600*24*30);

    private static Map<String, TokenType> values = new HashMap<>();

    static {
        for (TokenType t :TokenType.values()) {
            values.put(t.name, t);
        }
    }

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

    public static TokenType parseName(String name) {
        return values.get(name);
    }



}

package jsonrpc.protocol.token;

import java.util.HashMap;
import java.util.Map;

public enum GrantType {
    PASSWORD("password"),
    REFRESH("refresh_token");

    static Map<String, GrantType> ENUM_MAP = new HashMap<>();


    static {

        for(GrantType value : GrantType.values()) {
            ENUM_MAP.put(value.value, value);
        }
    }

    private String value;

    GrantType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static GrantType parse(String s) {

        return ENUM_MAP.get(s);
    }
}

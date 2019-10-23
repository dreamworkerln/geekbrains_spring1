package jsonrpc.protocol.dto.userinfo.request;


import jsonrpc.protocol.dto.base.jrpc.JrpcParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

public class UserProfile extends JrpcParam {

    private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private String email;
    private String nickname;

    protected UserProfile() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {return nickname;}

    public void setNickname(String nickname) {this.nickname = nickname;}

    // more getters and setters...
}

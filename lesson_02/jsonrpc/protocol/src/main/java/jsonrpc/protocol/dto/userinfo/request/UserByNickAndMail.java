package jsonrpc.protocol.dto.userinfo.request;


import jsonrpc.protocol.dto.base.jrpc.JrpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

public class UserByNickAndMail extends JrpcRequest {

    private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private String email;
    private String nickname;

    protected UserByNickAndMail() {}

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

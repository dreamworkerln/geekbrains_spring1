package jsonrpc.protocol.dto.userinfo.request;

import jsonrpc.protocol.dto.base.jrpc.JrpcParameter;

public class UserByColor extends JrpcParameter {

    private String color;

    protected UserByColor() {}

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}

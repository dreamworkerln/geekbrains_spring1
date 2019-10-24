package jsonrpc.protocol.dto.userinfo.response;

import jsonrpc.protocol.dto.Cat;
import jsonrpc.protocol.dto.base.jrpc.JrpcResponse;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class UserDto extends JrpcResponse {

    private String email;
    private String info;
    private List<Cat> pets = new ArrayList<>();
    private Instant date;
    private long epoch;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
        this.epoch = date.toEpochMilli();
    }

    public long getEpoch() {
        return epoch;
    }

    public List<Cat> getPets() {
        return pets;
    }

    public void setPets(List<Cat> pets) {
        this.pets = pets;
    }
}

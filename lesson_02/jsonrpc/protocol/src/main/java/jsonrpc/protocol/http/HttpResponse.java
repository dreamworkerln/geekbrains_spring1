package jsonrpc.protocol.http;

import jsonrpc.protocol.jrpc.JrpcBase;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.beans.Transient;

@Data
public class HttpResponse {


    protected HttpStatus status;
    private JrpcBase result;

    //HttpResponse() {}
    public HttpResponse(HttpStatus status) {
        this.status = status;
    }

    public HttpResponse(HttpStatus status, JrpcBase result) {
        this.status = status;
        this.result = result;
    }

    //@Transient

    // --------------------------------------------------------------

//
//    public static HttpResponse forbidden() {
//
//        return new HttpResponse(HttpStatus.FORBIDDEN);
//    }
//
//    public static HttpOkResponse ok() {
//
//        return new HttpOkResponse();
//    }
//
//    public static HttpResponse body() {
//
//        HttpResponse result = new HttpErrorResponse();
//        result.status =  HttpStatus.BAD_REQUEST;
//        return result;
//    }

}


/*




 */
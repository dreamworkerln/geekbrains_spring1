package jsonrpc.utils;

@SuppressWarnings({"WeakerAccess", "unused"})
public class RestFactory {

    public static Rest getRest() {
        return new Rest();
    }

    public static Rest getRest(int timeout) {
        return new Rest(timeout);
    }


    public static Rest getRest(boolean checkCert, boolean throwOnError) {
        return new Rest(checkCert, throwOnError);
    }

    public static Rest getRest(boolean checkCert, boolean throwOnError, int timeout) {
        return new Rest(checkCert, throwOnError, timeout);
    }
}




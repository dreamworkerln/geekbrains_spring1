package jsonrpc.resourceserver.service;

public class InvalidLogicException extends RuntimeException {

    public InvalidLogicException() {
    }

    public InvalidLogicException(String s) {
        super(s);
    }

    public InvalidLogicException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public InvalidLogicException(Throwable throwable) {
        super(throwable);
    }
}

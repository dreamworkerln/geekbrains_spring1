package jsonrpc.server.handlers.base;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Make the annotation available at runtime:
@Retention(RetentionPolicy.RUNTIME)


@Target(ElementType.METHOD)
public @interface JrpcHandler {
    /**
     * Method name, coming from json request 'method' param value
     */
    String method();
}

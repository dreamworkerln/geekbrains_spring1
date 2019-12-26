package jsonrpc.resourceserver.controller.jrpc.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark class as jrpc request handler
 * <br>
 * This allow to find appropriate handler for request (mapping)
 */

// Make the annotation available at runtime:
@Retention(RetentionPolicy.RUNTIME)
// Allow to use only on types:
@Target(ElementType.TYPE)
public @interface JrpcController {

    String path();
}

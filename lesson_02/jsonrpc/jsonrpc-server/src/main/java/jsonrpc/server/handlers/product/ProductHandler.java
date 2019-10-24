package jsonrpc.server.handlers.product;


import jsonrpc.server.handlers.base.JrpcController;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static jsonrpc.server.configuration.SpringConfiguration.MAIN_ENTITIES_PATH;

@Component
@Scope("prototype")
@JrpcController(path = MAIN_ENTITIES_PATH + "product")
public class ProductHandler {


}

package jsonrpc.authjwtserver.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class TestController {
    
    @RequestMapping("/test")
    public String hello() {
        return "Hello World";
    }
}


package jsonrpc.authserver.controller;


import jsonrpc.authserver.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

    private final TokenService tokenService;

    @Autowired
    public AdminController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/admin/test")
	public String hello() {
		return "Hello World";
	}

}

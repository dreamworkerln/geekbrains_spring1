package jsonrpc.authserver.controller;


import jsonrpc.authserver.config.AuthType;
import jsonrpc.authserver.config.aspect.ValidAuthenticationType;
import jsonrpc.authserver.entities.Role;
import jsonrpc.authserver.entities.User;
import jsonrpc.authserver.entities.token.AccessToken;
import jsonrpc.authserver.entities.token.RefreshToken;
import jsonrpc.authserver.service.TokenService;
import jsonrpc.authserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/admin/")
public class AdminController {

    private final TokenService tokenService;
    private final UserService userService;

    @Autowired
    public AdminController(TokenService tokenService, UserService userService) {
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @PostMapping("/test")
    @ValidAuthenticationType({AuthType.BASIC_AUTH, AuthType.BEARER_ACCESS})
    @Secured(Role.ADMIN)
	public ResponseEntity<String> hello() {

        return  ResponseEntity.ok("Hello World");
	}


    @PostMapping("/user/revoke_token")
    @ValidAuthenticationType({AuthType.BASIC_AUTH, AuthType.BEARER_ACCESS})
    @Secured(Role.ADMIN)
    public ResponseEntity revokeToken(@RequestBody String userName) {

        HttpStatus status;

        User user = userService.findByName(userName);

        if (user == null) {
            status = HttpStatus.NOT_FOUND;
        }
        else {
           tokenService.deleteByUser(user);
            status = HttpStatus.OK;
        }
        
        return new ResponseEntity(status);
    }







}

package jsonrpc.authjwtserver.controller;


import jsonrpc.authjwtserver.entities.Token;
import jsonrpc.authjwtserver.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@CrossOrigin() - already configured in websecurity
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

    @PostMapping("/admin/token/approve")
    public ResponseEntity approveToken(@Param("id") Long id) {

        ResponseEntity result;


        if (tokenService.findById(id).isPresent()) {
            result = new ResponseEntity(null, HttpStatus.OK);
            tokenService.approveToken(id);
        }
        else {
            result = new ResponseEntity(null, HttpStatus.NOT_FOUND);
        }

        return result;
    }






}

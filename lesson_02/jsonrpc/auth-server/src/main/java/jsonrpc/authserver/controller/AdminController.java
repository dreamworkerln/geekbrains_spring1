package jsonrpc.authserver.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {


    private final TokenStore tokenStore;

    public AdminController(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    // GET tokens
    // curl -v -u clientId:secret -X POST localhost:9001/oauth/token --data "grant_type=password&username=user&password=pass" ; echo
    // curl -v -u clientId:secret -X POST localhost:9001/oauth/token --data "grant_type=password&username=admin&password=nooneguessthis" ; echo

    // Refresh
    // curl -u clientId:secret -i localhost:9001/oauth/token  --data "grant_type=refresh_token&refresh_token=[refreshToken]" ; echo

    //Access resource
    //curl -i localhost:9101/ddt -H "Authorization: Bearer [userAccessToken]" ; echo

    // Administrate (only admin token can)
    // curl -i "localhost:9001/admin/tokens/revokeAccessToken/[userAccessToken]" -H "Authorization: Bearer [adminAccessToken]" ; echo

    // trash
    // curl -u clientId:secret  -i localhost:9001/oauth/token  --data "grant_type=refresh_token&refresh_token=REFRESH_TOKEN" ; echo
    // curl -u admin:nooneguessthis -i localhost:9001/admin/tokens/clientId ; echo



    

    // DOCS
    // https://www.baeldung.com/spring-security-oauth-revoke-tokens


    @GetMapping("/check")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<Principal> get(final Principal principal) {
        return ResponseEntity.ok(principal);
    }


    // test purposes
    @GetMapping("/ddt")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> ddt() {
        return ResponseEntity.ok("DDT");
    }




    // Не прибивает протухшие токены, все равно отображает их у пользователя. Типа он есть, хотя протухший.
    @GetMapping( value = "/tokens/{clientId}")
    @Secured("ROLE_ADMIN")
    public List<String> getTokens(@PathVariable("clientId") String clientId) {
        List<String> tokenList = new ArrayList<>();
        Collection<OAuth2AccessToken> tokens = tokenStore.findTokensByClientId(clientId);  //tokenStore.findTokensByClientId("sampleClientId");
        if (tokens != null){
            for (OAuth2AccessToken token:tokens){
                tokenList.add(token.getValue());
            }
        }
        return tokenList;
    }





    @GetMapping( value = "/tokensByName/{clientId}/{userName}")
    @Secured("ROLE_ADMIN")
    public List<String> getTokens(@PathVariable("clientId") String clientId,
                                  @PathVariable("userName") String userName) {
        List<String> tokenList = new ArrayList<>();
        Collection<OAuth2AccessToken> tokens = tokenStore.findTokensByClientIdAndUserName(clientId, userName);  //tokenStore.findTokensByClientId("sampleClientId");
        if (tokens != null){
            for (OAuth2AccessToken token:tokens){
                tokenList.add(token.getValue());
            }
        }
        return tokenList;
    }



    @RequestMapping(method = RequestMethod.GET, value = "/tokens/revokeRefreshToken/{tokenId:.*}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> revokeRefreshToken(@PathVariable String tokenId) {

        ResponseEntity<String> httpResponse = new ResponseEntity<>("NOT_FOUND", HttpStatus.NOT_FOUND);

        if (tokenStore instanceof JdbcTokenStore){

            JdbcTokenStore jdbcTokenStore = (JdbcTokenStore) tokenStore;
            if (jdbcTokenStore.readRefreshToken(tokenId) != null) {

                httpResponse = new ResponseEntity<>("OK", HttpStatus.OK);
                jdbcTokenStore.removeRefreshToken(tokenId);
            }
        }

        return httpResponse;
    }


    @RequestMapping(method = RequestMethod.GET, value = "/tokens/revokeAccessToken/{tokenId:.*}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> revokeAccessToken(@PathVariable String tokenId) {

        ResponseEntity<String> httpResponse = new ResponseEntity<>("NOT_FOUND", HttpStatus.NOT_FOUND);

        if (tokenStore instanceof JdbcTokenStore){

            JdbcTokenStore jdbcTokenStore = (JdbcTokenStore) tokenStore;
            if (jdbcTokenStore.readAccessToken(tokenId) != null) {

                httpResponse = new ResponseEntity<>("OK", HttpStatus.OK);
                jdbcTokenStore.removeAccessToken(tokenId);
            }
        }

        return httpResponse;
    }

}



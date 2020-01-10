package jsonrpc.authserver.config;

import io.jsonwebtoken.Claims;
import jsonrpc.authserver.config.misc.RequestScopeBean;
import jsonrpc.authserver.entities.Role;
import jsonrpc.authserver.entities.token.Token;
import jsonrpc.authserver.service.JwtTokenService;
import jsonrpc.authserver.service.TokenService;
import jsonrpc.protocol.token.TokenType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;

import static jsonrpc.authserver.config.SpringConfiguration.ISSUER;

import static jsonrpc.utils.Utils.rolesToGrantedAuthority;



//@Component
//RegistrationBean
//@WebFilter(urlPatterns = "/hellow")
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final UserDetailsService userDetailsService;
    private final JwtTokenService jwtTokenService;
    private final TokenService tokenService;
    private final RequestScopeBean requestScopeBean;

    @Autowired
    public JwtRequestFilter(@Qualifier("myUserDetailsService") UserDetailsService userDetailsService, JwtTokenService jwtTokenService, TokenService tokenService, RequestScopeBean requestScopeBean) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenService = jwtTokenService;
        this.tokenService = tokenService;
        this.requestScopeBean = requestScopeBean;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws ServletException, IOException {

        // get header "Authorization"
        final String requestTokenHeader = request.getHeader("Authorization");


        // JWT Token is in the form "Bearer token". Remove Bearer word and get only the Token
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {

            String jwtToken = requestTokenHeader.substring(7);

            try {

                // decode & !validate! token
                Claims claims = jwtTokenService.decodeJWT(jwtToken);
                Token token = null;

                // TOKEN issued by ME // не особо нужно
                // TOKEN Is NOT EXPIRED
                // TOKEN is PRESENT in my DB (NOT deleted/blacklisted)
                if (claims.getIssuer().equals(ISSUER) &&
                    tokenIsActive(claims) &&
                    (token = findToken(claims))!=null) {


                    UsernamePasswordAuthenticationToken authToken = getAuthToken(claims);
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    //TokenWebAuthenticationDetails details = new TokenWebAuthenticationDetails(request);
                    //details.setToken(token);
                    //authToken.setDetails(details);

                    // save token
                    requestScopeBean.setToken(token);

                    // After setting the Authentication in the context, we specify
                    // that the current user is authenticated.
                    // So it passes the Spring Security Configurations successfully.
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }

            } catch (Exception e) {
                log.info("JWT Token not valid: ", e);
            }

        }

        chain.doFilter(request, response);
    }

    // ------------------------------------------------------------


//    /**
//     * Token is known to server(not deleted/blacklisted)
//     */
//    private boolean tokenIsPresent(Claims claims) {
//
//        boolean result = false;
//
//        TokenType type = TokenType.parseName((String) claims.get("type"));
//
//        Long tokenId = Long.valueOf(claims.getId());
//
//        if (type == TokenType.ACCESS) {
//            result = tokenService.findAccessToken(tokenId) != null;
//        } else if (type == TokenType.REFRESH) {
//            result = tokenService.findRefreshToken(tokenId) != null;
//        }
//
//        return result;
//    }


    private boolean tokenIsActive(Claims claims) {
        return claims.getExpiration().toInstant().toEpochMilli() > Instant.now().toEpochMilli();

    }


    private Token findToken(Claims claims) {

        Token result = null;

        TokenType type = TokenType.parseName((String) claims.get("type"));

        Long tokenId = Long.valueOf(claims.getId());

        if (type == TokenType.ACCESS) {
            result = tokenService.findAccessToken(tokenId);
        } else if (type == TokenType.REFRESH) {
            result = tokenService.findRefreshToken(tokenId);
        }
        return result;
    }




    private UsernamePasswordAuthenticationToken getAuthToken(Claims claims) {

        UsernamePasswordAuthenticationToken result = null;

        TokenType type = TokenType.parseName((String)claims.get("type"));

        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());


        if (userDetails != null) {

            Collection<? extends GrantedAuthority> grantedAuthority = null;

            if (type == TokenType.ACCESS) {
                // get User default authorities
                grantedAuthority = userDetails.getAuthorities();
            }
            else if (type == TokenType.REFRESH) {
                // Set ROLE_REFRESH only
                grantedAuthority = rolesToGrantedAuthority(Collections.singletonList(Role.REFRESH));
            }

            // configure Spring Security to manually set authentication
            result = new UsernamePasswordAuthenticationToken(userDetails, null, grantedAuthority);
        }
        return result;
    }


}


// КАК СОХРАНЯТЬ ДАННЫЕ В СЕССИЮ ------------------------------------------------------------------------------

//        Сохраняем claims в сессии
//    request.getSession().setAttribute("claims", claims);


//        Читаем где-то дальше в другом месте
//    ServletRequestAttributes attr = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
//    HttpSession session = attr.getRequest().getSession();  // getSession(true) == create new if not exists
//    Claims claims = (Claims)session.getAttribute("claims");
// -------------------------------------------------------------------------------------------------------------




//    private Token createToken(Claims claims) {
//
//
//
//        if (type==TokenType.ACCESS) {
//
//
//
//
//        }
//        else if (type==TokenType.REFRESH) {
//
//
//
//        }
//    }




//    @Override
//    public void doFilter(ServletRequest requestBase, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        HttpServletRequest request = (HttpServletRequest)requestBase;


//    private Token findToken(Claims claims) {
//
//        Token result = null;
//
//        TokenType type = TokenType.parseName((String)claims.get("type"));
//        Long tokenId = Long.valueOf(claims.getId());
//
//        if (type == TokenType.ACCESS) {
//            result = tokenService.findAccessToken(tokenId);
//        }
//        else if(type == TokenType.REFRESH) {
//            result = tokenService.findRefreshToken(tokenId);
//        }
//
//        return result;
//    }

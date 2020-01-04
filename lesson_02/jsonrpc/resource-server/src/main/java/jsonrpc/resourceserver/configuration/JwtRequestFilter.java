package jsonrpc.resourceserver.configuration;

import io.jsonwebtoken.Claims;
import jsonrpc.protocol.http.TokenType;
import jsonrpc.resourceserver.service.jwt.JwtTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
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
import java.util.List;
import java.util.stream.Collectors;

import static jsonrpc.resourceserver.configuration.SpringConfiguration.ISSUER;

//@Component
//RegistrationBean
//@SuppressWarnings("Duplicates")
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    //private final UserDetailsService userDetailsService;
    private final JwtTokenService jwtTokenService;

    @Autowired
    public JwtRequestFilter(UserDetailsService userDetailsService, JwtTokenService jwtTokenService) {
        //this.userDetailsService = userDetailsService;
        this.jwtTokenService = jwtTokenService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");


        // JWT Token is in the form "Bearer token". Remove Bearer word and get only the Token
        if (requestTokenHeader != null &&
                requestTokenHeader.startsWith("Bearer ")) {

            String jwtToken = requestTokenHeader.substring(7);

            try {
                // validating token
                Claims claims = jwtTokenService.decodeJWT(jwtToken);

                // checking access rights / token validity
                if (claims.getIssuer().equals(ISSUER) &&
                        claims.get("type").equals(TokenType.ACCESS.getValue()) &&
                        claims.getExpiration().toInstant().toEpochMilli() >= Instant.now().toEpochMilli()) {

                    @SuppressWarnings("unchecked")
                    List<SimpleGrantedAuthority> authorities = ((List<String>)claims.get("authorities"))
                            .stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

                    UserDetails userDetails = new User(claims.getSubject(), "[PROTECTED]", authorities);

                    // if jwt is valid configure Spring Security to manually set authentication
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // After setting the Authentication in the context, we specify
                    // that the current user is authenticated. So it passes the Spring Security Configurations successfully.
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                }

            } catch (Exception e) {

                log.info("JWT Token not valid: ", e);
            }

        }

        chain.doFilter(request, response);
    }


}




//                    @SuppressWarnings("unchecked")
//                    List<Map<String,String>> authoritiesMap = (List<Map<String,String>>) claims.get("authorities");
//
//                    //List<GrantedAuthority> authorities =
//
//                    List<SimpleGrantedAuthority> authorities =
//
//                            authoritiesMap.stream().flatMap(sl -> sl.values().stream())
//                                    .map(SimpleGrantedAuthority::new).collect(Collectors.toList());




//    @Override
//    public void doFilter(ServletRequest requestBase, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        HttpServletRequest request = (HttpServletRequest)requestBase;
package jsonrpc.authserver.config;

import io.jsonwebtoken.Claims;
import jsonrpc.authserver.service.JwtTokenService;
import jsonrpc.authserver.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

import static jsonrpc.authserver.config.SpringConfiguration.ISSUER;

//@Component
//RegistrationBean
//@WebFilter(urlPatterns = "/hellow")
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final UserDetailsService userDetailsService;
    private final JwtTokenService jwtTokenService;
    private final TokenService tokenService;

    @Autowired
    public JwtRequestFilter(UserDetailsService userDetailsService, JwtTokenService jwtTokenService, TokenService tokenService) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenService = jwtTokenService;
        this.tokenService = tokenService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // get header "Authorization"
        final String requestTokenHeader = request.getHeader("Authorization");


        // JWT Token is in the form "Bearer token". Remove Bearer word and get only the Token
        if (requestTokenHeader != null &&
            requestTokenHeader.startsWith("Bearer ")) {

            String jwtToken = requestTokenHeader.substring(7);

            try {

                // decode & !validate! token
                Claims claims = jwtTokenService.decodeJWT(jwtToken);

                // token id
                Long id = Long.valueOf(claims.getId());

                // TOKEN issued by ME // не особо нужно
                // TOKEN Is NOT ROTTEN
                // TOKEN is PRESENT in my DB (NOT BLACKLISTED)
                if (claims.getIssuer().equals(ISSUER) &&
                    claims.getExpiration().toInstant().toEpochMilli() > Instant.now().toEpochMilli() &&
                    tokenService.findById(id) != null) {

                    UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());

                    if (userDetails != null) {

                        // Сохраняем token в сессии
                        request.getSession().setAttribute("claims", claims);

                        // configure Spring Security to manually set authentication
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());


                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        // After setting the Authentication in the context, we specify
                        // that the current user is authenticated.
                        // So it passes the Spring Security Configurations successfully.
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }

            } catch (Exception e) {

                log.info("JWT Token not valid: ", e);
            }

        }

        chain.doFilter(request, response);
    }

}


//    @Override
//    public void doFilter(ServletRequest requestBase, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        HttpServletRequest request = (HttpServletRequest)requestBase;

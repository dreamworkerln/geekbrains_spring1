package jsonrpc.authjwtserver.config;

import io.jsonwebtoken.Claims;
import jsonrpc.authjwtserver.entities.Role;
import jsonrpc.authjwtserver.service.JwtTokenService;
import jsonrpc.protocol.http.TokenType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static jsonrpc.authjwtserver.config.SpringConfiguration.ISSUER;

//@Component
//RegistrationBean
//@WebFilter(urlPatterns = "/hellow")
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final UserDetailsService userDetailsService;
    private final JwtTokenService jwtTokenService;

    @Autowired
    public JwtRequestFilter(UserDetailsService userDetailsService, JwtTokenService jwtTokenService) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenService = jwtTokenService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

//    @Override
//    public void doFilter(ServletRequest requestBase, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        HttpServletRequest request = (HttpServletRequest)requestBase;

        final String requestTokenHeader = request.getHeader("Authorization");


        // JWT Token is in the form "Bearer token". Remove Bearer word and get only the Token
        if (requestTokenHeader != null &&
            requestTokenHeader.startsWith("Bearer ")) {

            String jwtToken = requestTokenHeader.substring(7);

            try {

                // Validate token
                Claims claims = jwtTokenService.decodeJWT(jwtToken);

                @SuppressWarnings("unchecked")
                Set<String> authorities = new HashSet<>(((List<String>)claims.get("authorities")));


                // checking access rights / token validity
                if (claims.getIssuer().equals(ISSUER) &&
                        claims.get("type").equals(TokenType.REFRESH.getValue()) &&
                        authorities.contains(Role.REFRESH) &&
                        claims.getExpiration().toInstant().toEpochMilli() >= Instant.now().toEpochMilli()) {


                    String username = claims.getSubject();

                    // Сохраняем token id в сессии
                    request.getSession(true).setAttribute("tokenId", claims.getId());


                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    if (userDetails != null) {

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

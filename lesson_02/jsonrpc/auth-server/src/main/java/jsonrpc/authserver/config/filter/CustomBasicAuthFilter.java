package jsonrpc.authserver.config.filter;

import jsonrpc.authserver.config.RequestScopeBean;
import jsonrpc.authserver.config.AuthType;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.invoke.MethodHandles;

@Component("customBasicAuthFilter")
public class CustomBasicAuthFilter extends OncePerRequestFilter {

    private final static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final UserDetailsService userDetailsService;
    private final RequestScopeBean requestScopeBean;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomBasicAuthFilter(@Qualifier("myUserDetailsService")UserDetailsService userDetailsService,
        RequestScopeBean requestScopeBean, PasswordEncoder passwordEncoder) {

        this.userDetailsService = userDetailsService;
        this.requestScopeBean = requestScopeBean;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws ServletException, IOException {

        // get header "Authorization"
        final String requestBasicHeader = request.getHeader("Authorization");


        // JWT Token is in the form "Bearer token". Remove Bearer word and get only the Token
        if (requestBasicHeader != null &&
            requestBasicHeader.startsWith("Basic ")) {

            try {

                String basicAuth = requestBasicHeader.substring(6);

                byte[] decodedBytes = Base64.decodeBase64(basicAuth);
                String decodedAuth = new String(decodedBytes);
                
                String[] splitted = decodedAuth.split(":", 2);
                String userName = splitted[0];
                String password = splitted[1];

                UsernamePasswordAuthenticationToken authToken = getAuthToken(userName, password);
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                requestScopeBean.setAuthType(AuthType.BASIC_AUTH);

                SecurityContextHolder.getContext().setAuthentication(authToken);

            } catch (Exception e) {
                log.info("Basic Auth string not valid: ", e);
            }
        }

        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthToken(String userName, String password) {

        UsernamePasswordAuthenticationToken result = null;
        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);

        if (userDetails != null &&
            passwordEncoder.matches(password, userDetails.getPassword())) {

            result = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        }
        return result;
    }

}
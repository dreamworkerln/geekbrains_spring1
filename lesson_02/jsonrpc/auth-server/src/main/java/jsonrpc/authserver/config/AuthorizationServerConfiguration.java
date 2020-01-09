package jsonrpc.authserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

import javax.sql.DataSource;
import java.io.Serializable;
import java.util.*;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    private final DataSource dataSource;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    private TokenStore tokenStore;

    public AuthorizationServerConfiguration(
            final DataSource dataSource, final PasswordEncoder passwordEncoder,
            final AuthenticationManager authenticationManager,
            final UserDetailsService userDetailsService) {
        this.dataSource = dataSource;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public TokenStore tokenStore() {
        if (tokenStore == null) {
            tokenStore = new JdbcTokenStore(dataSource);
        }
        return tokenStore;
    }


    @Bean
    public DefaultTokenServices tokenServices(final ClientDetailsService clientDetailsService) {

        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setReuseRefreshToken(false);
        tokenServices.setTokenStore(tokenStore());
        tokenServices.setClientDetailsService(clientDetailsService);
        tokenServices.setAuthenticationManager(createPreAuthProvider());

        return tokenServices;
    }

    // Not a bean!
    private ProviderManager createPreAuthProvider() {
        PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
        provider.setPreAuthenticatedUserDetailsService(new UserDetailsByNameServiceWrapper<>(userDetailsService));
        return new ProviderManager(Collections.singletonList(provider));
    }


    @Override
    public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSource);
    }

    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) {

        endpoints.tokenStore(tokenStore())
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                .tokenServices(tokenServices(endpoints.getClientDetailsService()));
    }


    // тут настройка только для OAuth2.0 endpoint'ов
    @Override
    public void configure(final AuthorizationServerSecurityConfigurer oauthServer) {

//        oauthServer.tokenKeyAccess("isAnonymous() || hasAuthority('ROLE_TRUSTED_CLIENT')")
//                .checkTokenAccess("hasAuthority('ROLE_TRUSTED_CLIENT')");

        

        oauthServer.passwordEncoder(passwordEncoder);
        oauthServer.tokenKeyAccess("permitAll()");
        oauthServer.checkTokenAccess("isAuthenticated()");
        //oauthServer.checkTokenAccess("permitAll()");


        //oauthServer.checkTokenAccess("hasAuthority('ROLE_RESOURCE') || hasAuthority('ROLE_ADMIN')");

        //oauthServer.checkTokenAccess("hasAuthority('ROLE_ADMIN')");

        //oauthServer.checkTokenAccess("isAuthenticated() && hasAuthority('ROLE_ADMIN')");
        //oauthServer.checkTokenAccess("isAuthenticated()");
        //oauthServer.allowFormAuthenticationForClients();



        //oauthServer.passwordEncoder(passwordEncoder)
        //        .tokenKeyAccess("permitAll()")
        //       .checkTokenAccess("isAuthenticated()");
        //
    }





    @Autowired
    private AuthorizationServerEndpointsConfiguration configuration;
}





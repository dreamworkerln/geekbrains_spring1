package jsonrpc.authserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@EnableWebSecurity
@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final DataSource dataSource;
    private PasswordEncoder passwordEncoder;
    private UserDetailsService userDetailsService;

    @Autowired
    public WebSecurityConfiguration(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        if (passwordEncoder == null) {
            passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        }
        return passwordEncoder;
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        if (userDetailsService == null) {
            userDetailsService = new JdbcDaoImpl();
            ((JdbcDaoImpl) userDetailsService).setDataSource(dataSource);
        }
        return userDetailsService;
    }


//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//
//
////        http
////                .authorizeRequests().antMatchers("/admin/ddt").permitAll()
////                .and()
////                .authorizeRequests().antMatchers("/admin/who").permitAll()
////                .and()
////                .httpBasic()
////                .and()
////                .csrf().disable();
//    }


//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//
//
//
//        //http.authorizeRequests().antMatchers("/admin/**")
//        //        //.access("hasRole('ROLE_ADMIN')")
//        //        .access("hasRole('ROLE_ADMIN')");
//                //.antMatchers("/**").denyAll();
////
////                http
////                .csrf().disable()
////                .exceptionHandling()
////                .authenticationEntryPoint(restAuthenticationEntryPoint)
////                .and()
////                .authorizeRequests()
////                .antMatchers("/api/foos").authenticated()
////                .antMatchers("/api/admin/**").hasRole("ADMIN")
////                .and()
////                .formLogin()
////                .successHandler(mySuccessHandler)
////                .failureHandler(myFailureHandler)
////                .and()
////                .logout();
//    }

}

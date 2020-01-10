package jsonrpc.authserver.config;

import jsonrpc.authserver.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)


public class MultipleWebSecurityConfig {

//    @Autowired
//    private final ApplicationContext context;

//    public MultipleWebSecurityConfig(ApplicationContext context) {
//        this.context = context;
//    }


//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        // configure AuthenticationManager so that it knows from where to load
//        // user for matching credentials
//        // Use BCryptPasswordEncoder
//        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }



    // Отключаем к хренам JwtRequestFilter, иначе spring boot автоматом запхнет его в servlet filter chain
    // https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto-disable-registration-of-a-servlet-or-filter
    // Хотите проще(в аннотации самого фильтра отключить)? - нате
    // https://github.com/spring-projects/spring-boot/issues/16500
    //
    // Как робят фильтры в spring(security)
    // https://habr.com/ru/post/346628/
    @Bean
    public FilterRegistrationBean<JwtRequestFilter> jwtRequestFilterRegistration(JwtRequestFilter filter) {
        FilterRegistrationBean<JwtRequestFilter> result = new FilterRegistrationBean<>(filter);
        result.setEnabled(false);
        return result;
    }


    // Не используется сейчас
    /**
     * Administration
     * Authorisation: Basic + Bearer
     */
    @Configuration
    @Order(1)
    public static class AdminWebSecurityConfig extends WebSecurityConfigurerAdapter {

        private final JwtRequestFilter jwtRequestFilter;

        @Autowired
        public AdminWebSecurityConfig(JwtRequestFilter jwtRequestFilter) {
            this.jwtRequestFilter = jwtRequestFilter;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {

            http.antMatcher("/admin/**").authorizeRequests().anyRequest().authenticated()
                    .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and().csrf().disable()
                    .httpBasic()
                    .and().addFilterBefore(jwtRequestFilter, BasicAuthenticationFilter.class);
        }
    }



    /**
     * Token operations
     * Authorisation: Basic + Bearer
     */
    @Configuration
    @Order(2)
    public static class TokenWebSecurityConfig extends WebSecurityConfigurerAdapter {

        private final JwtRequestFilter jwtRequestFilter;

        @Autowired
        public TokenWebSecurityConfig(JwtRequestFilter jwtRequestFilter) {
            this.jwtRequestFilter = jwtRequestFilter;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {


            http.antMatcher("/oauzz/token**/**").authorizeRequests().anyRequest().authenticated()
                    .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and().csrf().disable()
                    .httpBasic()
                    .and().addFilterBefore(jwtRequestFilter, BasicAuthenticationFilter.class);


        }
    }

}



//            http.antMatcher("/admin/**").authorizeRequests().anyRequest().authenticated()
//                    .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                    .and().csrf().disable()
//                    .httpBasic();






//        @Bean
//        @Override
//        public AuthenticationManager authenticationManagerBean() throws Exception {
//            return super.authenticationManagerBean();
//        }





//    @Bean
//    public UserDetailsService userDetailsService() {
//
//        // user/password
//        UserDetails user =
//                User.builder()
//                        .username("user")
//                        .password("{bcrypt}$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6")
//                        .roles("USER")
//                        .disabled(false)
//                        .build();
//
//
//        return new InMemoryUserDetailsManager(user);
//    }



//            String[] paths = {"/admin", "/oauzz/tokenz"};
//            http.authorizeRequests().antMatchers(paths).authenticated()
//                    .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                    .and().csrf().disable()
//                    .httpBasic();

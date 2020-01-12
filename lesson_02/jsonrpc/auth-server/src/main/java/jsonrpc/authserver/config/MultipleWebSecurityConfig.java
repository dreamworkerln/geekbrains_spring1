package jsonrpc.authserver.config;

import jsonrpc.authserver.config.filter.CustomBasicAuthFilter;
import jsonrpc.authserver.config.filter.JwtRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutFilter;

import java.lang.invoke.MethodHandles;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
    prePostEnabled = true,
    securedEnabled = true,
    jsr250Enabled = true)


public class MultipleWebSecurityConfig {

    private final static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }



    // Отключаем к хренам JwtRequestFilter, иначе spring boot автоматом запхнет его в filterChain (servlet filter chain)
    // https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto-disable-registration-of-a-servlet-or-filter
    // (Этот фильтр должен работать только в springSecurityFilterChain)
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

    @Bean
    public FilterRegistrationBean<CustomBasicAuthFilter> customBasicFilterRegistration(CustomBasicAuthFilter filter) {
        FilterRegistrationBean<CustomBasicAuthFilter> result = new FilterRegistrationBean<>(filter);
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
        private final CustomBasicAuthFilter customBasicAuthFilter;

        @Autowired
        public AdminWebSecurityConfig(JwtRequestFilter jwtRequestFilter, CustomBasicAuthFilter customBasicAuthFilter) {
            this.jwtRequestFilter = jwtRequestFilter;
            this.customBasicAuthFilter = customBasicAuthFilter;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {

            http.antMatcher("/admin/**").authorizeRequests().anyRequest().authenticated()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().csrf().disable()
                .addFilterAfter(jwtRequestFilter, LogoutFilter.class)
                .addFilterAfter(customBasicAuthFilter, LogoutFilter.class);
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
        private final CustomBasicAuthFilter customBasicAuthFilter;


        @Autowired
        public TokenWebSecurityConfig(JwtRequestFilter jwtRequestFilter, CustomBasicAuthFilter customBasicAuthFilter) {
            this.jwtRequestFilter = jwtRequestFilter;
            this.customBasicAuthFilter = customBasicAuthFilter;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {


            //http.antMatcher("/oauzz/token**/**").authorizeRequests().anyRequest().authenticated()
            http.antMatcher("/oauzz/token/**").authorizeRequests().anyRequest().authenticated()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().csrf().disable()
                .addFilterAfter(jwtRequestFilter, LogoutFilter.class)
                .addFilterAfter(customBasicAuthFilter, LogoutFilter.class);
        }

    }

}

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






//        @Override
//        protected void configure(AuthenticationManagerBuilder auth) {
//            auth.authenticationProvider(authProvider);
//        }


//        @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
//        @Override
//        public AuthenticationManager authenticationManagerBean() throws Exception {
//            return super.authenticationManagerBean();
//        }



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
//                .httpBasic();


//                .httpBasic()
//                .and()
//                .addFilterBefore(jwtRequestFilter, BasicAuthenticationFilter.class)
//                .addFilterAt(customBasicAuthFilter, BasicAuthenticationFilter.class);

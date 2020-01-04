package jsonrpc.resourceserver.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtRequestFilter jwtRequestFilter;

    @Autowired
    public WebSecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
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
    

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.antMatcher("/api/**").authorizeRequests().anyRequest().authenticated()
                .and().addFilterBefore(jwtRequestFilter, BasicAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().csrf().disable();

//        http.authorizeRequests().anyRequest().authenticated()
//                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and().csrf().disable()
//                .httpBasic();
    }

}

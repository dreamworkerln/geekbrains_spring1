package jsonrpc.authserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

//    // Deny Access on Missing @PreAuthorize(@Secured) to Spring Controller Methods by Default
//    // https://www.baeldung.com/spring-deny-access
//    @Override
//    protected MethodSecurityMetadataSource customMethodSecurityMetadataSource() {
//        return new CustomPermissionAllowedMethodSecurityMetadataSource();
//    }


}

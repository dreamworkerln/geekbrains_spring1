package jsonrpc.server.configuration;

import org.hibernate.validator.constraints.Length;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


/**
 * Custom properties - for education purposes
 * https://www.baeldung.com/configuration-properties-in-spring-boot
 */
//
//@ContextConfiguration
@Configuration
@PropertySource("classpath:configprops.properties")
@ConfigurationProperties(prefix = "mail")
// Калечит исходные классы, заворачивая их в прокси, потом вся логика, построенная на .getClass()
// и отладчик(просмотр значений) идет лесом
@Validated
public class ConfigProperties {

    @NotBlank
    private String hostName;

    @Min(1025)
    @Max(65536)
    private int port;

    @Pattern(regexp = "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,6}$")
    private String from;


    private Credentials credentials;

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }


    // ===========================================================

    @Validated
    public static class Credentials {

        @Length(max = 4, min = 1)
        private String authMethod;
        private String username;
        private String password;
        private String token;



        public String getAuthMethod() {
            return authMethod;
        }

        public void setAuthMethod(String authMethod) {
            this.authMethod = authMethod;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getToken() {return token;}

        public void setToken(String token) {this.token = token;}
    }

}



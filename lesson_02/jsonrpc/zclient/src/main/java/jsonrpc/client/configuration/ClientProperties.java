package jsonrpc.client.configuration;

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
 * Custom properties
 * https://www.baeldung.com/configuration-properties-in-spring-boot
 */
//
//@ContextConfiguration
@Configuration
@PropertySource("classpath:client.properties")
// Калечит исходные классы, заворачивая их в прокси, потом вся логика, построенная на .getClass()
// и отладчик(просмотр значений) идет лесом
@Validated
@ConfigurationProperties//("zzz")
public class ClientProperties {

    private Credentials credentials;

    private Server server;

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }



    // =========================================================

    @Validated
    //@ConfigurationProperties(prefix = "server")
    public static class Credentials {

        @Length(max = 4, min = 1)
        private String authMethod;
        private String username;
        private String password;
        @NotBlank
        private String token;


        public String getAuthMethod() {
            return authMethod;
        }

        public void setAuthMethod(String authMethod) {this.authMethod = authMethod;}

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

        @Override
        public String toString() {
            return "Credentials{" +
                   "authMethod='" + authMethod + '\'' +
                   ", username='" + username + '\'' +
                   ", password='" + password + '\'' +
                   ", token='" + token + '\'' +
                   '}';
        }
    }


    @Validated
    //@ConfigurationProperties(prefix = "server")
    public static class Server {


        @NotBlank
        private String hostName;

        @Min(1025)
        @Max(65536)
        private int port;

//        @Pattern(regexp = "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,6}$")
//        private String from;


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


        @Override
        public String toString() {
            return "Server{" +
                   "hostName='" + hostName + '\'' +
                   ", port=" + port +
                   '}';
        }
    }

}



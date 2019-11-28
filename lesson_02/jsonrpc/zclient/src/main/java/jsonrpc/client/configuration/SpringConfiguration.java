package jsonrpc.client.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jsonrpc.utils.Rest;
import jsonrpc.utils.RestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@ComponentScan("jsonrpc.protocol")
public class SpringConfiguration {

    private final ClientProperties clientProperties;

    @Autowired
    public SpringConfiguration(ClientProperties clientProperties) {
        this.clientProperties = clientProperties;
    }


    @Bean
    public ObjectMapper objectMapper() {

        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        return mapper;
    }


    @Bean
    @Scope("prototype")
    public Rest rest() {

        final String TOKEN = clientProperties.getCredentials().getToken();

        Rest result = RestFactory.getRest(true, true);
        result.setCustomHeader("token", TOKEN);
        return result;
    }
}

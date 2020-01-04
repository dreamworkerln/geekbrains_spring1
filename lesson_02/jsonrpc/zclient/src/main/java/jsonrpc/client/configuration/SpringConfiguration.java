package jsonrpc.client.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jsonrpc.utils.RestTemplateFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan("jsonrpc.protocol")
public class SpringConfiguration {

    //private final ClientProperties clientProperties;

//    @Autowired
//    public SpringConfiguration(ClientProperties clientProperties) {
//        this.clientProperties = clientProperties;
//    }


    @Bean
    public ObjectMapper objectMapper() {

        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        return mapper;
    }

    @Bean
    public RestTemplate restTemplate() {

        return RestTemplateFactory.getRestTemplate(100000);
    }


//    @Bean
//    @Scope("prototype")
//    public Rest rest() {
//
//        //final String TOKEN = clientProperties.getCredentials().getToken();
//
//        Rest result = RestFactory.getRest(true, true, 1000000);
//        //result.setCustomHeader("token", TOKEN);
//        return result;
//    }
}

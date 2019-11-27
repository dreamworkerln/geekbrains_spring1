package jsonrpc.server.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("jsonrpc.protocol")  // scan components from protocol module
public class SpringConfiguration {

    //public static final String MAIN_ENTITIES_PATH = "shop.entities";

    @Bean
    // ObjectMapper is threadsafe
    public ObjectMapper objectMapper() {

        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());  // allow convertation to/from Instant

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        //mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

        //mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //mapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);
        return mapper;
    }




/*  @Bean
    @Scope("singleton")// ModelMapper is threadsafe
    public ModelMapper modelMapper() {

        ModelMapper result = new ModelMapper();

        // https://github.com/modelmapper/modelmapper/issues/212#issuecomment-293493836
        //
        // It enables field matching, set the access level as private,
        // and the source naming convention as Mutator to avoid multiple matches with getters.
//        result.getConfiguration().setFieldMatchingEnabled(true)
//                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
//                .setSourceNamingConvention(NamingConventions.JAVABEANS_MUTATOR);

        result.getConfiguration().setMethodAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PROTECTED);

        return result;
    }*/


    // ----------------------------------------------------------
}

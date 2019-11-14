package jsonrpc.server.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class SpringConfiguration {

    public static final String MAIN_ENTITIES_PATH = "shop.entities";

    @Bean
    @Scope("singleton")
    //ToDo: ObjectMapper is threadsafe, so need setup several ObjectMapper with different config on startup
    // And set bean scope to "singleton"
    public ObjectMapper objectMapper() {

        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());  // allow convertation to/from Instant

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

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


    public static class Controller {

        public static class Handlers {

            public static class Shop {

                public static final String ORDER = "shop.entities.order";
            }

        }
    }
}

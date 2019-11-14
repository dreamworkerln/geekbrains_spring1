package jsonrpc.server.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@EnableWebMvc
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final ObjectMapper objectMapper;

    @Autowired
    public WebMvcConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Custom Jackson Config for Spring Boot
     * https://gdpotter.com/2017/05/24/custom-spring-mvc-jackson/
     *
     *  Эта штука позволяет настроить ObjectMapper, который Spring Boot юзает для MVC
     *  Она будет использовать уже сконфигурированный ObjectMapper,
     *  (или default, если не создать соответствующий бин)
     *  Поэтому используем objectMapper.copy() и доводим напильником ObjectMapper MVC,
     *  чтобы не изменять глобальный(default) ObjectMapper (если это нужно).
     *
     *  Здесь default ObjectMapper в этом примере конфигурируется отдельно
     *  в бине SpringConfiguration.objectMapper()
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        ObjectMapper webObjectMapper = objectMapper.copy();
        //webObjectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        converters.add(new MappingJackson2HttpMessageConverter(webObjectMapper));
    }
}


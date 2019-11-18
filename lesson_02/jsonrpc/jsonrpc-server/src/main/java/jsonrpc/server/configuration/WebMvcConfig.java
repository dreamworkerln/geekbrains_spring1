package jsonrpc.server.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

// @EnableWebMvc - если расскоментировать, то
// сделает настройки с нуля и не создаст 8 конвертеров (видимо создает по-умочанию)
// и из-за отсутствия к-то конвертера это работать не будет
// Поэтому оставим все по-умолчанию, а нужный нам
// MappingJackson2HttpMessageConverter заменим на тот, который хотим.

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
     *  Она будет использовать уже сконфигурированный ObjectMapper(ваш bean, если он есть),
     *  (или default, если не создать соответствующий бин)
     *  Поэтому используем objectMapper.copy() и доводим напильником ObjectMapper MVC,
     *  чтобы не изменять ObjectMapper из bean(если это нужно).
     *
     *  Здесь ObjectMapper в этом примере конфигурируется отдельно
     *  в бине SpringConfiguration.objectMapper()
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

        // удаляем нагенерированные json конвертеры по-умолчанию (у меня аж 2 штуки)
        converters.removeIf(converter -> converter instanceof MappingJackson2HttpMessageConverter);

        //  Добавляем  конвертер из бина
        ObjectMapper webObjectMapper = objectMapper.copy();
        // И можем дальше кастомизировать его клон, не трогая оригинальный биновый objectMapper
        //webObjectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        converters.add(new MappingJackson2HttpMessageConverter(webObjectMapper));
    }
}


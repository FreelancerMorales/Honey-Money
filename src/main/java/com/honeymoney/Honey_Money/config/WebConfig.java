package com.honeymoney.Honey_Money.config;

import java.nio.charset.StandardCharsets;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.stream()
            .filter(converter -> converter instanceof MappingJackson2HttpMessageConverter)
            .forEach(converter -> {
                ((MappingJackson2HttpMessageConverter) converter).setSupportedMediaTypes(
                    List.of(
                        MediaType.APPLICATION_JSON,
                        new MediaType("application", "json", StandardCharsets.UTF_8)
                    )
                );
            });
    }
}
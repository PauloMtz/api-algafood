package com.algafood.core.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    // implementa o CORS globalmente

    // digitar addCorsMappings que o editor irá sugerir o override

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            //.allowedOrigins("*")
            //.allowedMethods("GET", "HEAD", "POST")
            .allowedMethods("*");
            //.maxAge(30) // segundos
    }
}

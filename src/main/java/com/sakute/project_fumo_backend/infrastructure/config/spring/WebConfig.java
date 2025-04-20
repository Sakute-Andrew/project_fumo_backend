package com.sakute.project_fumo_backend.infrastructure.config.spring;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${web.origin.path}")
    private String frontendUrl;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(frontendUrl)  // Дозволити запити з цього домену
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // Дозволяємо методи
                .allowedHeaders("*");  // Дозволяємо всі заголовки
    }
}


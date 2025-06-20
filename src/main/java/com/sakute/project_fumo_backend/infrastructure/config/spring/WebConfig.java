package com.sakute.project_fumo_backend.infrastructure.config.spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${frontend.url}")
    private String frontUrl;
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(frontUrl)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Ваші існуючі маппінги
        registry.addResourceHandler("/photos/posts/**")
                .addResourceLocations("file:data/photos/posts/");

        registry.addResourceHandler("/photos/users/**")
                .addResourceLocations("file:data/photos/users/");

        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:data/files/");

        // Додатковий маппінг для повного шляху
        registry.addResourceHandler("/data/**")
                .addResourceLocations("file:data/");
    }


}


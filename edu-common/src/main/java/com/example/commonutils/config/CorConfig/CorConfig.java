package com.example.commonutils.config.CorConfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(false)
                .allowedMethods("POST" , "GET" , "PUT" , "OPTIONS" , "DELETE")
                .allowedOrigins("*")
                .allowedHeaders("*");
    }
}

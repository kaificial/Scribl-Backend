package com.birthday.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${cors.allowed-origins}")
    private String allowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // split the origins by comma so we can add multiple
        String[] origins = allowedOrigins.split(",");

        registry.addMapping("/**")
                .allowedOrigins(origins)
                // allow all the standard methods
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                // allow any headers they send
                .allowedHeaders("*")
                // allow credentials (cookies, auth headers)
                .allowCredentials(true)
                // cache the results for an hour
                .maxAge(3600);
    }
}

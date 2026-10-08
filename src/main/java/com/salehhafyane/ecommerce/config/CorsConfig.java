package com.salehhafyane.ecommerce.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Centralized CORS configuration for all controller endpoints.
 * Replaces reliance on scattered {@code @CrossOrigin} annotations with a single,
 * explicit policy allowing the Angular dev server origin.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    // Origin of the Angular development server.
    private static final String ANGULAR_DEV_ORIGIN = "http://localhost:4200";

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(ANGULAR_DEV_ORIGIN)
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}

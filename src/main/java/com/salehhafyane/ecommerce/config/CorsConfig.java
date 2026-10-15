// One place that lets the Angular dev server call the API from localhost:4200.
package com.salehhafyane.ecommerce.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Applies to all /api/** endpoints: allowed origin, methods and credentials in one policy.
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    // Where the Angular dev server runs.
    private static final String ANGULAR_DEV_ORIGIN = "http://localhost:4200";

    // Registers the CORS rules; credentials (JWT header) are allowed for this origin only.
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

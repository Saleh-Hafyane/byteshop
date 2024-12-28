package com.salehhafyane.ecommerce.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security configuration class for customizing Spring Security.
 * It sets up JWT-based authentication, session management, and access control rules.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // Custom JWT authentication filter to process and validate JWT tokens.
    private final JwtAuthFilter jwtAuthFilter;

    // Authentication provider for user authentication and password encoding.
    private final AuthenticationProvider authenticationProvider;

    // Defines the security filter chain, configuring how HTTP requests are secured.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF protection since the application uses JWT for authentication.
                .csrf(AbstractHttpConfigurer::disable)

                // Configure request authorization rules.
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/api/checkout/**") // Protect the `/api/checkout/**` endpoints.
                            .authenticated()                     // Require authentication for these endpoints.
                            .anyRequest()                       // All other endpoints.
                            .permitAll();                       // Allow access without authentication.
                })

                // Configure session management to use stateless sessions (since JWT is used).
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })

                // Set the authentication provider for verifying user credentials.
                .authenticationProvider(authenticationProvider)

                // Add the custom JWT authentication filter before the default username/password filter.
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        // Build and return the security filter chain.
        return http.build();
    }
}

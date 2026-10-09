package com.salehhafyane.ecommerce.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Security configuration class for customizing Spring Security.
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
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                // Configure request authorization rules.
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/api/v1/auth/**").permitAll() // Auth endpoints are public.
                            .requestMatchers("/error").permitAll()      // Allow Spring's error dispatch (needed for GlobalExceptionHandler).
                            .requestMatchers("/api/checkout/**").hasRole("USER") // Protect checkout endpoints.
                            .requestMatchers("/api/user/**").hasRole("USER")     // Protect user order history endpoints.
                            .requestMatchers("/api/orders/**").hasAnyRole("USER", "ADMIN") // Order details: owner or admin (ownership enforced in service).
                            .requestMatchers("/api/admin/**").hasRole("ADMIN")   // Protect admin endpoints.
                            .anyRequest().permitAll();
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

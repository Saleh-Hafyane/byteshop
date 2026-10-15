// Decides which endpoints need which role, and switches security to stateless JWT mode.
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

// The single source of truth for URL access rules.
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // Injected so every request passes through JWT validation first.
    private final JwtAuthFilter jwtAuthFilter;

    // Verifies username/password on /api/v1/auth/authenticate.
    private final AuthenticationProvider authenticationProvider;

    // Builds the filter chain: no sessions, no CSRF, rules below, JWT filter in front.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF is unnecessary because tokens are sent in headers, not cookies.
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(ex -> ex
                        // Any unauthenticated hit on a protected route answers plain 401.
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                // URL rules, checked top to bottom.
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/api/v1/auth/**").permitAll() // register/login are public
                            .requestMatchers("/error").permitAll()      // error dispatch must stay reachable for the exception handler
                            .requestMatchers("/api/checkout/**").hasRole("USER") // only logged-in customers may buy
                            .requestMatchers("/api/user/**").hasRole("USER")     // order history is per-account
                            .requestMatchers("/api/orders/**").hasAnyRole("USER", "ADMIN") // details: owner or admin, checked in the service
                            .requestMatchers("/api/admin/**").hasRole("ADMIN")   // admin area is admin-only
                            .anyRequest().permitAll(); // catalog and image reads stay public
                })

                // JWT means nothing is stored server-side between requests.
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })

                // Wires in the credentials checker used by the auth endpoint.
                .authenticationProvider(authenticationProvider)

                // Token validation happens before Spring's own username/password filter.
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

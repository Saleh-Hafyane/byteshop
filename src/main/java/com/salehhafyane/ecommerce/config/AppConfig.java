package com.salehhafyane.ecommerce.config;

import com.salehhafyane.ecommerce.entity.User;
import com.salehhafyane.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
/**
 * This configuration class sets up the authentication-related beans for the application.
 * It integrates with Spring Security to define how users are loaded and authenticated.
 */
@Configuration
@RequiredArgsConstructor
public class AppConfig {
    // Repository to fetch user details from the database
    private final UserRepository userRepository;
 /**
 * Defines a custom UserDetailsService bean.
 * The UserDetailsService is used by Spring Security to load user-specific data during authentication.
 */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("User not found"));

    }
/**
 * Configures the AuthenticationProvider bean.
 * This bean is responsible for processing authentication requests.
 */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService()); // Set the custom UserDetailsService
        authProvider.setPasswordEncoder(passwordEncoder()); // Set the password encoder
        return authProvider;
    }
/**
 * Configures the AuthenticationManager bean.
 * The AuthenticationManager is used by Spring Security to manage authentication operations.
 */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    /**
 * Configures the PasswordEncoder bean.
 * The PasswordEncoder is used to encode and decode passwords securely.
 */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

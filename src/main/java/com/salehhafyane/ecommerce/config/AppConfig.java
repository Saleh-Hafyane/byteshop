// Wires up Spring Security's building blocks: user lookup, password hashing, login manager.
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
// Provides the beans the login flow and JwtAuthFilter depend on.
@Configuration
@RequiredArgsConstructor
public class AppConfig {
    // Used to fetch account rows from db_user during login.
    private final UserRepository userRepository;
    // Loads one account by username; login fails with UsernameNotFoundException when unknown.
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("User not found"));

    }
    // Combines the user lookup above with password checking so Spring Security can log people in.
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService()); // where accounts come from
        authProvider.setPasswordEncoder(passwordEncoder()); // how passwords get compared
        return authProvider;
    }
    // Entry point used by AuthService to verify a username/password pair.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    // One-way bcrypt hashing: plaintext passwords are never stored.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

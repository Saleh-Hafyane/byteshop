// Seeds the first admin account on startup so the shop can be managed right away.
package com.salehhafyane.ecommerce.config;

import com.salehhafyane.ecommerce.entity.Role;
import com.salehhafyane.ecommerce.entity.User;
import com.salehhafyane.ecommerce.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
// Runs once after every boot; does nothing when the admin already exists.
@Configuration
public class DataInitializer {

    // Creates admin / admin123 (bcrypt-hashed) with the ADMIN role if no such username exists yet.
    @Bean
    CommandLineRunner init(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("admin@example.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole(Role.ADMIN);
                userRepository.save(admin);
            }
        };
    }
}
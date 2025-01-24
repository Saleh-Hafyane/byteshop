package com.salehhafyane.ecommerce.auth;

import com.salehhafyane.ecommerce.config.JwtService;
import com.salehhafyane.ecommerce.entity.Role;
import com.salehhafyane.ecommerce.entity.User;
import com.salehhafyane.ecommerce.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.support.Repositories;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

// This service class handles authentication-related operations, such as user registration and login.
@Service
@AllArgsConstructor
public class AuthService {

    // Repository to interact with the user database.
    private final UserRepository userRepository;

    // Utility for encoding passwords securely.
    private final PasswordEncoder passwordEncoder;

    // Service responsible for generating and managing JWT tokens.
    private final JwtService jwtService;

    // AuthenticationManager for handling authentication requests.
    private final AuthenticationManager authenticationManager;

    /*
    * Registers a new user by saving their information in the database, encrypting the password,
    * and assigning a default role. It then generates a JWT token for the user.
    */
    public AuthenticationResponse register(RegisterRequest request) {
        // Build a new User entity based on the registration request data.
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))  // Encrypts the user's password.
                .role(Role.USER)  // Assign default role of USER.
                .build();

        // Save the new user to the database.
        userRepository.save(user);
        // Add the role to the extra claims
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", user.getRole());
        // Generate a JWT token for the newly registered user.
        var jwtToken = jwtService.generateToken(extraClaims,user);

        // Return the authentication response with the JWT token and username.
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .username(request.getUsername())
                .build();
    }

    /*
     * Authenticates an existing user by verifying the username and password.
     * Generates and returns a JWT token if authentication is successful.
    */
    public AuthenticationResponse authenticate(AuthRequest request) {
        // Authenticate the user using the provided credentials.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // Retrieve the user details from the database. Throws an exception if the user is not found.
        var user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        // add the role to the extra claims
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", user.getRole());
        // Generate a JWT token for the authenticated user.
        var jwtToken = jwtService.generateToken(extraClaims,user);

        // Return the authentication response with the JWT token and username.
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .username(request.getUsername())
                .role(user.getRole())
                .build();
    }
}

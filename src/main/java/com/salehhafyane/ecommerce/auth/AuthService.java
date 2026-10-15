// Handles registration and login: checks duplicates, hashes passwords, issues JWTs.
package com.salehhafyane.ecommerce.auth;

import com.salehhafyane.ecommerce.config.JwtService;
import com.salehhafyane.ecommerce.entity.Role;
import com.salehhafyane.ecommerce.entity.User;
import com.salehhafyane.ecommerce.exceptions.UserAlreadyExistsException;
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

// The only place that hands out tokens.
@Service
@AllArgsConstructor
public class AuthService {

    // Account table.
    private final UserRepository userRepository;

    // Hashes new passwords and compares login attempts.
    private final PasswordEncoder passwordEncoder;

    // Builds the JWT sent back to the browser.
    private final JwtService jwtService;

    // Verifies username/password pairs on login.
    private final AuthenticationManager authenticationManager;

    // Creates the account (with duplicate checks), then immediately returns a token.
    public AuthenticationResponse register(RegisterRequest request) {
        // Username and email are unique; report both clashes in one response.
        Map<String, String> duplicateErrors = new HashMap<>();
        if (userRepository.existsByUsername(request.getUsername())) {
            duplicateErrors.put("username", "Username already exists: " + request.getUsername());
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            duplicateErrors.put("email", "Email already in use: " + request.getEmail());
        }
        if (!duplicateErrors.isEmpty()) {
            throw new UserAlreadyExistsException("Registration failed due to existing credentials", duplicateErrors);
        }

        // Brand-new account, always role USER — admin is never chosen at signup.
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))  // stored only as a hash
                .role(Role.USER)  // customers can't self-assign admin
                .build();

        userRepository.save(user);
        // Role travels inside the token so the frontend can react without an extra call.
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", user.getRole());
        var jwtToken = jwtService.generateToken(extraClaims,user);

        // Everything the login page needs: token plus who logged in.
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .username(request.getUsername())
                .build();
    }

    // Verifies the credentials through AuthenticationManager, then issues a fresh token.
    public AuthenticationResponse authenticate(AuthRequest request) {
        // Wrong username or password throws here and becomes a 401.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // Loaded only after the password check passed.
        var user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        // Embed the role in the token claims as during registration.
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", user.getRole());
        var jwtToken = jwtService.generateToken(extraClaims,user);

        // Token, username and role for the frontend session.
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .username(request.getUsername())
                .role(user.getRole())
                .build();
    }
}

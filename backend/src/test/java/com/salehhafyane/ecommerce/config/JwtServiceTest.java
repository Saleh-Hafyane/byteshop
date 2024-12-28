package com.salehhafyane.ecommerce.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

class JwtServiceTest {

    private JwtService jwtService;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtService = new JwtService();

        // Create a mock user
        userDetails = User.builder()
                .username("testuser")
                .password("password")
                .authorities("ROLE_USER")
                .build();
    }

    @Test
    void testGenerateAndExtractUsername() {
        // Arrange
        String token = jwtService.generateToken(userDetails);

        // Act: Extract username
        String extractedUsername = jwtService.extractUsername(token);

        // Assert that the username matches
        assertEquals(userDetails.getUsername(), extractedUsername, "The extracted username should match the original.");
    }

    @Test
    void testTokenValidation() {
        // Arrange
        String token = jwtService.generateToken(userDetails);

        // Act: Validate token
        boolean isValid = jwtService.isTokenValid(token, userDetails);

        // Assert
        assertTrue(isValid, "The generated token should be valid for the given user.");
    }

    @Test
    void testTokenExpiration() throws InterruptedException {
        // Arrange: Generate token with a short expiration time (override generateToken temporarily)
        String shortLivedToken = Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 2000)) // Expires in 2 seconds
                .signWith(jwtService.getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

        // Validate token immediately
        boolean isValidBeforeExpiration = jwtService.isTokenValid(shortLivedToken, userDetails);
        assertTrue(isValidBeforeExpiration, "The token should be valid before expiration.");

        // Wait for the token to expire
        Thread.sleep(3000); // Sleep for 3 seconds

        // Validate token after expiration
        boolean isValidAfterExpiration = jwtService.isTokenValid(shortLivedToken, userDetails);
        assertFalse(isValidAfterExpiration, "The token should not be valid after expiration.");
    }

    @Test
    void testExtractExpiration() {
        // Arrange
        String token = jwtService.generateToken(userDetails);

        // Act
        Date expiration = jwtService.extractExpiration(token);

        // Assert
        assertNotNull(expiration, "The expiration date should not be null.");
        assertTrue(expiration.after(new Date()), "The expiration date should be in the future.");
    }
}

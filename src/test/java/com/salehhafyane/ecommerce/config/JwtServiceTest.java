// Verifies JWT signing, validation and expiry handling against a real JwtService instance.
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

// Uses a plain Spring Security User as the token subject, no Spring context needed.
class JwtServiceTest {

    private JwtService jwtService;

    @Mock
    private UserDetails userDetails;

    // Fresh service and user for every test.
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtService = new JwtService();

        // Simple account whose username is embedded in tokens.
        userDetails = User.builder()
                .username("testuser")
                .password("password")
                .authorities("ROLE_USER")
                .build();
    }

    // The subject written into the token must come back out unchanged.
    @Test
    void testGenerateAndExtractUsername() {
        String token = jwtService.generateToken(userDetails);

        String extractedUsername = jwtService.extractUsername(token);

        assertEquals(userDetails.getUsername(), extractedUsername, "The extracted username should match the original.");
    }

    // A freshly generated token must pass validation for its own user.
    @Test
    void testTokenValidation() {
        String token = jwtService.generateToken(userDetails);

        boolean isValid = jwtService.isTokenValid(token, userDetails);

        assertTrue(isValid, "The generated token should be valid for the given user.");
    }

    // A token must flip from valid to invalid once its expiry passes.
    @Test
    void testTokenExpiration() throws InterruptedException {
        // Hand-built token that dies after 2 seconds.
        String shortLivedToken = Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 2000)) // dies in 2 seconds
                .signWith(jwtService.getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

        // Still alive right away.
        boolean isValidBeforeExpiration = jwtService.isTokenValid(shortLivedToken, userDetails);
        assertTrue(isValidBeforeExpiration, "The token should be valid before expiration.");

        // Wait past the expiry moment.
        Thread.sleep(3000);

        // Now rejected.
        boolean isValidAfterExpiration = jwtService.isTokenValid(shortLivedToken, userDetails);
        assertFalse(isValidAfterExpiration, "The token should not be valid after expiration.");
    }
}

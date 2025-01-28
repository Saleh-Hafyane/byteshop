package com.salehhafyane.ecommerce.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

// This service class provides methods for generating, validating, and extracting data from JWT tokens.
@Service
public class JwtService {

    // Secret key used to sign and verify JWT tokens.
    private static final String SECRET_KEY = "e143b6139ef946b0320a744e44a36e47eb6e6f8997a8facb7ff22127cecba24c";

    // Extracts the username (subject) from a JWT token.
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Validates the JWT token by checking the username and ensuring it is not expired.
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    // Extracts a specific claim from a JWT token using a provided resolver function.
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Parses the JWT token and retrieves all claims stored in it.
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .setAllowedClockSkewSeconds(5) // Allow 5 seconds of clock skew
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Checks if the JWT token is expired.
    boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extracts the expiration date from the JWT token.
    Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Generates a new JWT token with the given user details and no extra claims.
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    // Generates a new JWT token with additional claims and the user details.
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {

        return Jwts.builder()
                .setClaims(extraClaims)  // Set any additional claims
                .setSubject(userDetails.getUsername())  // Set the subject as the username
                .setIssuedAt(new Date(System.currentTimeMillis()))  // Set the current date as the issue date
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))  // Set expiration date (1 day)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)  // Sign the token with the secret key
                .compact();
    }

    // Decodes the secret key and returns it as a Key object for signing/verifying tokens.
    Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

// Creates, signs and verifies the JSON Web Tokens used for every authenticated request.
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

// Tokens last one day and are signed with HS256 using the hex secret below.
@Service
public class JwtService {

    // Shared secret that both signs new tokens and verifies old ones.
    private static final String SECRET_KEY = "e143b6139ef946b0320a744e44a36e47eb6e6f8997a8facb7ff22127cecba24c";

    // Reads the subject claim, which stores the username.
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // A token is valid when it names this user and hasn't passed its expiry.
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    // Pulls any single claim out of the token, e.g. expiration or subject.
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Parses and signature-checks the whole token; throws when forged or malformed.
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .setAllowedClockSkewSeconds(5) // small grace period so tiny clock differences don't reject tokens
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // True once the token's expiration date is in the past.
    boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Reads the exp claim from the token.
    Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Convenience overload when no extra claims are needed.
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    // Builds the signed token: username as subject, issued now, expires in 24 hours.
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {

        return Jwts.builder()
                .setClaims(extraClaims)  // role or other extra data if needed
                .setSubject(userDetails.getUsername())  // who this token belongs to
                .setIssuedAt(new Date(System.currentTimeMillis()))  // issued now
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))  // valid for 1 day
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)  // HMAC signature with the secret
                .compact();
    }

    // Turns the hex secret into the HMAC key used to sign and verify.
    Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

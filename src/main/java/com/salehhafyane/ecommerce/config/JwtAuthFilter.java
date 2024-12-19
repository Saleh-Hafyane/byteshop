package com.salehhafyane.ecommerce.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
/**
 * JwtAuthFilter is a Spring Security filter that processes each HTTP request once.
 * It validates the JWT token from the "Authorization" header and sets authentication
 * in the security context if the token is valid.
 */
@Component
@RequiredArgsConstructor // Generates a constructor for all final fields (jwtService and userDetailsService)
public class JwtAuthFilter extends OncePerRequestFilter {

    // Dependency for handling JWT-related operations
    private final JwtService jwtService;

    // Dependency for loading user details (from the database)
    private final UserDetailsService userDetailsService;

    /**
     * The core logic for filtering each incoming request.
     * Validates the JWT token and sets the security context if the token is valid.
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        // Extract the "Authorization" header from the HTTP request
        final String authorizationHeader = request.getHeader("Authorization");

        // If the header is null or does not start with "Bearer ", pass the request down the filter chain
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwtToken = authorizationHeader.replace("Bearer ", "");

        final String username = jwtService.extractUsername(jwtToken);

        // Check if a username is extracted and if the current security context is not already authenticated
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // Validate the JWT token against the loaded user details
            if (jwtService.isTokenValid(jwtToken, userDetails)) {

                // Create an authentication token with user details and authorities
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // Set additional details from the HTTP request
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set the authentication in the security context
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Pass the request to the next filter in the chain
        filterChain.doFilter(request, response);
    }
}

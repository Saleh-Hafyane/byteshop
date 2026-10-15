// Reads the JWT from each request's Authorization header and logs the user in for that request.
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
// Runs once per request before controllers, so @AuthenticationPrincipal works downstream.
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    // Signs and checks the tokens.
    private final JwtService jwtService;

    // Loads the account behind the token's username.
    private final UserDetailsService userDetailsService;

    // Validates the token and, if good, puts the user into Spring's security context.
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        // The browser sends credentials as "Authorization: Bearer <token>".
        final String authorizationHeader = request.getHeader("Authorization");

        // No token on this request: let it through anonymously (public endpoints still work).
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwtToken = authorizationHeader.replace("Bearer ", "");

        String username = null;
        try {
            username = jwtService.extractUsername(jwtToken);
        } catch (Exception e) {
            // Broken or expired token: continue without authentication instead of failing the request.
        }

        // Only bother when we got a username and nobody is authenticated yet.
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // Signature, match and expiry must all check out.
            if (jwtService.isTokenValid(jwtToken, userDetails)) {

                // Principal for this request, carrying the account's ROLE_* authorities.
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // Keeps request details (IP, session id) for auditing.
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // From here on, security rules and @AuthenticationPrincipal see this user.
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Continue towards the controller either way.
        filterChain.doFilter(request, response);
    }
}

// Public endpoints for creating an account and logging in.
package com.salehhafyane.ecommerce.controller;

import com.salehhafyane.ecommerce.auth.AuthRequest;
import com.salehhafyane.ecommerce.auth.AuthService;
import com.salehhafyane.ecommerce.auth.AuthenticationResponse;
import com.salehhafyane.ecommerce.auth.RegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Both routes return a JWT on success, which the browser then stores as "token".
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {
    private final AuthService authService;
    // POST /register — creates the account and logs it in straight away.
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }
    // POST /authenticate — checks credentials and returns the JWT, 401 when wrong.
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
}

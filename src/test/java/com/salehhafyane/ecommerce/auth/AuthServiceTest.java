package com.salehhafyane.ecommerce.auth;

import com.salehhafyane.ecommerce.config.JwtService;
import com.salehhafyane.ecommerce.entity.Role;
import com.salehhafyane.ecommerce.entity.User;
import com.salehhafyane.ecommerce.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authService = new AuthService(userRepository, passwordEncoder, jwtService, authenticationManager);
    }

    @Test
    void testRegister() {
        // Mock input data
        RegisterRequest request = new RegisterRequest();
        request.setFirstname("first");
        request.setLastname("last");
        request.setUsername("firstlast");
        request.setEmail("firstlast@example.com");
        request.setPassword("password123");

        // Mock encoded password and JWT token
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(jwtService.generateToken(any(User.class))).thenReturn("testJwtToken");

        // Call register method
        AuthenticationResponse response = authService.register(request);

        // Verify interactions
        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode(request.getPassword());
        verify(jwtService, times(1)).generateToken(any(User.class));

        // Assert response
        assertNotNull(response);
        assertEquals("testJwtToken", response.getToken());
        assertEquals("firstlast", response.getUsername());
    }

    @Test
    void testAuthenticate() {
        // Mock input data
        AuthRequest request = new AuthRequest();
        request.setUsername("firstlast");
        request.setPassword("password123");

        // Mock user and JWT token
        User mockUser = User.builder()
                .firstname("first")
                .lastname("last")
                .username("firstlast")
                .password("encodedPassword")
                .role(Role.USER)
                .build();

        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(mockUser));
        when(jwtService.generateToken(mockUser)).thenReturn("testJwtToken");

        // Call authenticate method
        AuthenticationResponse response = authService.authenticate(request);

        // Verify interactions
        verify(authenticationManager, times(1)).authenticate(any());
        verify(userRepository, times(1)).findByUsername(request.getUsername());
        verify(jwtService, times(1)).generateToken(mockUser);

        // Assert response
        assertNotNull(response);
        assertEquals("testJwtToken", response.getToken());
        assertEquals("firstlast", response.getUsername());
    }

    @Test
    void testAuthenticate_UserNotFound() {
        // Mock input data
        AuthRequest request = new AuthRequest();
        request.setUsername("unknown");
        request.setPassword("password123");

        // Mock user repository behavior
        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.empty());

        // Assert exception
        assertThrows(Exception.class, () -> authService.authenticate(request));

        // Verify interactions
        verify(authenticationManager, times(1)).authenticate(any());
        verify(userRepository, times(1)).findByUsername(request.getUsername());
        verify(jwtService, never()).generateToken(any());
    }
}

package com.salehhafyane.ecommerce.auth;

import com.salehhafyane.ecommerce.config.JwtService;
import com.salehhafyane.ecommerce.entity.Role;
import com.salehhafyane.ecommerce.entity.User;
import com.salehhafyane.ecommerce.exceptions.UserAlreadyExistsException;
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

import static org.mockito.ArgumentMatchers.anyMap;

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
        when(jwtService.generateToken(anyMap(), any(User.class))).thenReturn("testJwtToken");

        // Call register method
        AuthenticationResponse response = authService.register(request);

        // Verify interactions
        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode(request.getPassword());
        verify(jwtService, times(1)).generateToken(anyMap(), any(User.class));

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
        when(jwtService.generateToken(anyMap(), eq(mockUser))).thenReturn("testJwtToken");

        // Call authenticate method
        AuthenticationResponse response = authService.authenticate(request);

        // Verify interactions
        verify(authenticationManager, times(1)).authenticate(any());
        verify(userRepository, times(1)).findByUsername(request.getUsername());
        verify(jwtService, times(1)).generateToken(anyMap(), eq(mockUser));

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

    @Test
    void testRegister_DuplicateUsernameOnly_ReturnsSingleFieldError() {
        RegisterRequest request = new RegisterRequest();
        request.setFirstname("first");
        request.setLastname("last");
        request.setUsername("taken");
        request.setEmail("free@example.com");
        request.setPassword("password123");

        when(userRepository.existsByUsername("taken")).thenReturn(true);
        when(userRepository.existsByEmail("free@example.com")).thenReturn(false);

        UserAlreadyExistsException ex = assertThrows(
                UserAlreadyExistsException.class,
                () -> authService.register(request)
        );

        assertEquals(1, ex.getFieldErrors().size());
        assertEquals("Username already exists: taken", ex.getFieldErrors().get("username"));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testRegister_DuplicateEmailOnly_ReturnsSingleFieldError() {
        RegisterRequest request = new RegisterRequest();
        request.setFirstname("first");
        request.setLastname("last");
        request.setUsername("freeuser");
        request.setEmail("taken@example.com");
        request.setPassword("password123");

        when(userRepository.existsByUsername("freeuser")).thenReturn(false);
        when(userRepository.existsByEmail("taken@example.com")).thenReturn(true);

        UserAlreadyExistsException ex = assertThrows(
                UserAlreadyExistsException.class,
                () -> authService.register(request)
        );

        assertEquals(1, ex.getFieldErrors().size());
        assertEquals("Email already in use: taken@example.com", ex.getFieldErrors().get("email"));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testRegister_DuplicateUsernameAndEmail_ReturnsBothFieldErrorsAtOnce() {
        RegisterRequest request = new RegisterRequest();
        request.setFirstname("first");
        request.setLastname("last");
        request.setUsername("taken");
        request.setEmail("taken@example.com");
        request.setPassword("password123");

        when(userRepository.existsByUsername("taken")).thenReturn(true);
        when(userRepository.existsByEmail("taken@example.com")).thenReturn(true);

        UserAlreadyExistsException ex = assertThrows(
                UserAlreadyExistsException.class,
                () -> authService.register(request)
        );

        // Both errors must be reported in a single response
        assertEquals(2, ex.getFieldErrors().size());
        assertEquals("Username already exists: taken", ex.getFieldErrors().get("username"));
        assertEquals("Email already in use: taken@example.com", ex.getFieldErrors().get("email"));
        verify(userRepository, never()).save(any(User.class));
    }
}

package com.salehhafyane.ecommerce.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Standard error format (using Java Record for clean code)
    public record ApiError(int status, String message, LocalDateTime timestamp, Map<String, String> errors) {}

    // Catches @Valid errors -> 400 Bad Request
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err ->
                fieldErrors.put(err.getField(), err.getDefaultMessage())
        );
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value(), "Validation Failed", LocalDateTime.now(), fieldErrors);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
    // Catches duplicate username/email errors -> 409 Conflict
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        Map<String, String> errors = new HashMap<>();
        if (ex.getField() != null) {
            errors.put(ex.getField(), ex.getMessage());
        }
        ApiError apiError = new ApiError(
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                LocalDateTime.now(),
                errors.isEmpty() ? null : errors
        );
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    // Catches database unique constraint violations (like concurrent registrations) -> 409 Conflict
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        ApiError apiError = new ApiError(
                HttpStatus.CONFLICT.value(),
                "Username or email already exists",
                LocalDateTime.now(),
                null
        );
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }
    // Catches bad credentials / user not found -> 401 Unauthorized
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleBadCredentials(AuthenticationException ex) {
        ApiError apiError = new ApiError(
                HttpStatus.UNAUTHORIZED.value(),
                "Invalid username or password",
                LocalDateTime.now(),
                null
        );

        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }
    // Catches IllegalArgumentException -> 400 Bad Request
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException ex) {
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                LocalDateTime.now(),
                null
        );
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    // Catches standard RuntimeExceptions -> 500 Internal Server Error
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> handleRuntimeException(RuntimeException ex) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), LocalDateTime.now(), null);
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
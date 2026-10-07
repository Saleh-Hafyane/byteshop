package com.salehhafyane.ecommerce.exceptions;

import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
// This exception is thrown when a user tries to register with a username or email that already exists in the system.
@Getter
public class UserAlreadyExistsException extends RuntimeException {
    private final String field;
    private final Map<String, String> fieldErrors;
    // Constructor for a general error with no specific field.
    public UserAlreadyExistsException(String message) {
        super(message); // Pass the message to the parent RuntimeException class.
        this.field = null;
        this.fieldErrors = Collections.emptyMap();
    }
    // Constructor for a specific field error (e.g., username or email). => legacy
    public UserAlreadyExistsException(String field, String message) {
        super(message);
        this.field = field;
        // Create a map with the field and its corresponding error message. If the field is null, return an empty map.
        this.fieldErrors = field != null ? Map.of(field, message) : Collections.emptyMap();
    }
    // Constructor for multiple field errors (e.g., both username and email).
    public UserAlreadyExistsException(String message, Map<String, String> fieldErrors) {
        super(message);
        // backward compatibility: if fieldErrors is not empty, set the first key as the field; otherwise, set it to null.
        this.field = fieldErrors != null && !fieldErrors.isEmpty() ? fieldErrors.keySet().iterator().next() : null;
        // Create a defensive copy of the fieldErrors map to prevent external modifications.
        this.fieldErrors = fieldErrors != null ? new HashMap<>(fieldErrors) : Collections.emptyMap();
    }
}

// Registration clash on username or email; carries per-field messages for the 409 response.
package com.salehhafyane.ecommerce.exceptions;

import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
// Thrown when a register attempt uses a username or email that already exists.
@Getter
public class UserAlreadyExistsException extends RuntimeException {
    private final String field; // single offending field, kept for older call sites
    private final Map<String, String> fieldErrors; // field name -> message, e.g. {"email": "Email already in use"}
    // General error without a specific field.
    public UserAlreadyExistsException(String message) {
        super(message);
        this.field = null;
        this.fieldErrors = Collections.emptyMap();
    }
    // Single-field error, e.g. only the username was taken.
    public UserAlreadyExistsException(String field, String message) {
        super(message);
        this.field = field;
        this.fieldErrors = field != null ? Map.of(field, message) : Collections.emptyMap();
    }
    // Multi-field error, e.g. username and email both taken at once.
    public UserAlreadyExistsException(String message, Map<String, String> fieldErrors) {
        super(message);
        this.field = fieldErrors != null && !fieldErrors.isEmpty() ? fieldErrors.keySet().iterator().next() : null;
        this.fieldErrors = fieldErrors != null ? new HashMap<>(fieldErrors) : Collections.emptyMap(); // copy so callers can't mutate it
    }
}

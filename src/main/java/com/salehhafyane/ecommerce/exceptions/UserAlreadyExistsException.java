package com.salehhafyane.ecommerce.exceptions;

import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
public class UserAlreadyExistsException extends RuntimeException {
    private final String field;
    private final Map<String, String> fieldErrors;

    public UserAlreadyExistsException(String message) {
        super(message);
        this.field = null;
        this.fieldErrors = Collections.emptyMap();
    }

    public UserAlreadyExistsException(String field, String message) {
        super(message);
        this.field = field;
        this.fieldErrors = field != null ? Map.of(field, message) : Collections.emptyMap();
    }

    public UserAlreadyExistsException(String message, Map<String, String> fieldErrors) {
        super(message);
        this.field = fieldErrors != null && !fieldErrors.isEmpty() ? fieldErrors.keySet().iterator().next() : null;
        this.fieldErrors = fieldErrors != null ? new HashMap<>(fieldErrors) : Collections.emptyMap();
    }
}

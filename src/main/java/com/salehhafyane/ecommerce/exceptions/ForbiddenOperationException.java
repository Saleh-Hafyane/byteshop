package com.salehhafyane.ecommerce.exceptions;

/**
 * Thrown when a user attempts to access or modify a resource they do not own
 * (and are not an admin for). Mapped to 403 by {@link GlobalExceptionHandler}.
 */
public class ForbiddenOperationException extends RuntimeException {

    public ForbiddenOperationException(String message) {
        super(message);
    }
}

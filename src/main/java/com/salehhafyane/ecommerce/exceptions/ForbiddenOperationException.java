// Thrown when someone touches a resource they don't own; the handler turns it into 403.
package com.salehhafyane.ecommerce.exceptions;

public class ForbiddenOperationException extends RuntimeException {

    public ForbiddenOperationException(String message) {
        super(message);
    }
}

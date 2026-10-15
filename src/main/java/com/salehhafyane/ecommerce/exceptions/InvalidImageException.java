// Thrown for rejected image uploads; mapped to 415 (or 404 for missing files).
package com.salehhafyane.ecommerce.exceptions;

public class InvalidImageException extends RuntimeException {
    public InvalidImageException(String message) {
        super(message);
    }
}

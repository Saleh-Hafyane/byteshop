package com.salehhafyane.ecommerce.exceptions;

/**
 * Thrown when an order cannot be found. Mapped to 404 by
 * {@link GlobalExceptionHandler}.
 */
public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(Long orderId) {
        super("Order not found: " + orderId);
    }
}

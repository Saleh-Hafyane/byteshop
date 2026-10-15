// Thrown when an order id doesn't exist; mapped to 404 by the global handler.
package com.salehhafyane.ecommerce.exceptions;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(Long orderId) {
        super("Order not found: " + orderId);
    }
}

package com.salehhafyane.ecommerce.entity;

/**
 * Lifecycle states of an order. Stored with {@code @Enumerated(EnumType.STRING)}
 * so the database holds the literal name (e.g. "PENDING"), never an ordinal.
 */
public enum OrderStatus {
    PENDING,
    CONFIRMED,
    SHIPPED,
    DELIVERED,
    CANCELLED
}

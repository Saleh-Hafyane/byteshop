// Lifecycle states of an order; stored as text like "PENDING", never as a number.
package com.salehhafyane.ecommerce.entity;

public enum OrderStatus {
    PENDING, // just placed, not processed yet
    CONFIRMED, // admin accepted the order
    SHIPPED, // handed to the courier
    DELIVERED, // reached the customer
    CANCELLED // stopped before delivery
}

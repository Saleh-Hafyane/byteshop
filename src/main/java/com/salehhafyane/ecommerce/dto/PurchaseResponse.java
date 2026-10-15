// Checkout result: the tracking number the order confirmation shows.
package com.salehhafyane.ecommerce.dto;

import lombok.Data;

// Sent back once after a successful purchase so the customer can track the order.
@Data
public class PurchaseResponse {
    private final String orderTrackingNumber; // server-generated UUID of the new order
}

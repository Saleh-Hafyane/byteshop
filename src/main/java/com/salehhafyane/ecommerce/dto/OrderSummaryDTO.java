// One-row order view used by the admin and my-orders lists.
package com.salehhafyane.ecommerce.dto;

import com.salehhafyane.ecommerce.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

// Flat summary with no nested entities: the customer is just their username.
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSummaryDTO {
    private Long id; // order id, also the route key for the details page
    private String orderTrackingNumber; // public tracking UUID
    private OrderStatus status; // shown as the row's badge
    private Integer totalQuantity; // number of items
    private BigDecimal totalPrice; // amount of the order
    private Date dateCreated; // used to sort lists newest first
    private String customerUsername; // who placed the order
}

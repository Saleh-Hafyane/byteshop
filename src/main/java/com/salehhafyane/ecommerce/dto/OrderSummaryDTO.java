package com.salehhafyane.ecommerce.dto;

import com.salehhafyane.ecommerce.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Flat read-model of an order for list views. Contains no nested entities:
 * the customer is represented by {@code customerUsername} only.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSummaryDTO {
    private Long id;
    private String orderTrackingNumber;
    private OrderStatus status;
    private Integer totalQuantity;
    private BigDecimal totalPrice;
    private Date dateCreated;
    private String customerUsername;
}

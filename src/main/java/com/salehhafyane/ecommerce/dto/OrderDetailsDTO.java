package com.salehhafyane.ecommerce.dto;

import com.salehhafyane.ecommerce.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Full read-model of an order: summary fields plus the shipping address and
 * the purchased items (with server-side product name snapshots). The customer
 * is represented by {@code customerUsername} only; no entities cross the
 * API boundary.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailsDTO {
    private Long id;
    private String orderTrackingNumber;
    private OrderStatus status;
    private Integer totalQuantity;
    private BigDecimal totalPrice;
    private Date dateCreated;
    private String customerUsername;
    private AddressDTO address;
    private List<OrderItemDTO> orderItems;
}

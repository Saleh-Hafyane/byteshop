// Full order view for the details page: summary plus address and item lines.
package com.salehhafyane.ecommerce.dto;

import com.salehhafyane.ecommerce.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

// Read-only model: the customer appears as a plain username, no entities in the JSON.
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailsDTO {
    private Long id; // database id of the order
    private String orderTrackingNumber; // UUID the customer tracks with
    private OrderStatus status; // current lifecycle state
    private Integer totalQuantity; // number of items bought
    private BigDecimal totalPrice; // amount paid
    private Date dateCreated; // when it was placed
    private String customerUsername; // who bought it
    private AddressDTO address; // shipping address
    private List<OrderItemDTO> orderItems; // the purchased lines with name snapshots
}

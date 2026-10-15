// Admin request body for changing an order's status; only known statuses pass parsing.
package com.salehhafyane.ecommerce.dto;

import com.salehhafyane.ecommerce.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// The enum type rejects typos like "SHIPPEDD" at JSON parsing time with a 400.
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusUpdateRequest {
    @NotNull(message = "Status is required")
    private OrderStatus status; // the status to switch to, e.g. SHIPPED
}

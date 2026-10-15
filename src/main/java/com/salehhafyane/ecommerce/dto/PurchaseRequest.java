// Complete checkout payload: address, totals and item lines, all validated.
package com.salehhafyane.ecommerce.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// Exactly three fields on purpose — no status or tracking number can be injected by the client.
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseRequest {
    @Valid
    @NotNull(message = "Address is required")
    private AddressDTO address; // where to deliver

    @Valid
    @NotNull(message = "Order is required")
    private OrderDTO order; // totals block, re-checked server-side

    @Valid
    @NotEmpty(message = "Order must contain at least one item")
    private List<OrderItemDTO> orderItems; // what is being bought
}

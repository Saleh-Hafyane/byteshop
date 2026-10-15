// Totals section of the checkout request; the server re-verifies them against the items.
package com.salehhafyane.ecommerce.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

// Carries the cart's grand total and item count from the browser to the checkout endpoint.
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    @NotNull(message = "Total quantity is required")
    @PositiveOrZero(message = "Total quantity must be zero or positive")
    private Integer totalQuantity; // sum of item quantities in the cart

    @NotNull(message = "Total price is required")
    @PositiveOrZero(message = "Total price must be zero or positive")
    private BigDecimal totalPrice; // cart grand total in the request
}

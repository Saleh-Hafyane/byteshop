// One item line in checkout requests and order responses, with server-enforced validation.
package com.salehhafyane.ecommerce.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

// Flat JSON version of OrderItem shared by the request and the response side.
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
    private String imageUrl; // product picture copied at purchase time

    @NotNull(message = "Unit price is required")
    @PositiveOrZero(message = "Unit price must be zero or positive")
    private BigDecimal unitPrice; // price per unit when bought

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity; // units bought, minimum 1

    @NotNull(message = "Product id is required")
    private Long productId; // which catalog product this line refers to

    // Filled from the catalog on the server; OrderMapper.toEntity ignores it, so the client can't fake it.
    private String productName;
}

// Shipping address part of the checkout request, validated for non-blank fields.
package com.salehhafyane.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Flat JSON version of Address — what the client sends, never a JPA entity.
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    @NotBlank(message = "City is required")
    private String city; // e.g. "Rabat"

    @NotBlank(message = "Full address is required")
    private String fullAddress; // street line the courier delivers to
}

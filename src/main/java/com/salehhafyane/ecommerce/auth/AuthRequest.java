// Login payload: just the username and password typed on the login form.
package com.salehhafyane.ecommerce.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Validated by @Valid in AuthController before it reaches the service.
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthRequest {
    @NotBlank(message = "Username is required")

    private String username; // login name of the account

    @NotBlank(message = "Password is required")
    private String password; // plaintext, checked against the stored bcrypt hash
}

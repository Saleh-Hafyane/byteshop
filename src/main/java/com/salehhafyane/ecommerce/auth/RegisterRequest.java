// Registration payload with field rules for name, username, email and password.
package com.salehhafyane.ecommerce.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Rejections here surface as a 400 with per-field messages before the service runs.
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {
    @NotBlank(message = "Firstname is required")
    @Size(min = 2, message = "Firstname must be at least 2 characters long")
    private String firstname; // given name of the new account
    @NotBlank(message = "Lastname is required")
    @Size(min = 2, message = "Lastname must be at least 2 characters long")
    private String lastname; // family name of the new account
    @NotBlank(message = "Username is required")
    @Size(min = 3, message = "Username must be at least 3 characters long")
    private String username; // unique login name

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email; // unique contact address

    @NotBlank(message = "Password is required")
    @Size(min = 5, message = "Password must be at least 5 characters long")
    private String password; // plaintext here, hashed before storage

}

package com.salehhafyane.ecommerce.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {
    private String firstname;
    private String lastname;
    private String username;
    private String email;

    private String password;

}

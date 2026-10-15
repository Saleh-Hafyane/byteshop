// What the browser gets back after a successful register or login.
package com.salehhafyane.ecommerce.auth;

import com.salehhafyane.ecommerce.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// The token is saved as "token" in localStorage and sent with every later request.
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationResponse {
    private String token; // the JWT to reuse on subsequent calls
    private String username; // who just logged in, shown in the header
    private Role role; // USER or ADMIN, used to show or hide admin menu items
}

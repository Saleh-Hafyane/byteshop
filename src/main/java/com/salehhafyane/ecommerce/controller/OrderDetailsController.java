// Single endpoint for the order details page, guarded by ownership or admin role.
package com.salehhafyane.ecommerce.controller;

import com.salehhafyane.ecommerce.dto.OrderDetailsDTO;
import com.salehhafyane.ecommerce.entity.User;
import com.salehhafyane.ecommerce.service.OrderQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

// Requires authentication; the caller's identity comes from the JWT.
@CrossOrigin
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderDetailsController {

    private final OrderQueryService orderQueryService;

    // GET /api/orders/{id} — full details; 404 when missing, 403 when it isn't your order.
    @GetMapping("/{id}")
    public OrderDetailsDTO getOrderDetails(@PathVariable Long id, @AuthenticationPrincipal User user) {
        return orderQueryService.getOrderDetails(id, user);
    }
}

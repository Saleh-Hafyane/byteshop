// Endpoint for the "My Orders" page: only the caller's own orders.
package com.salehhafyane.ecommerce.controller;

import com.salehhafyane.ecommerce.dto.OrderSummaryDTO;
import com.salehhafyane.ecommerce.entity.User;
import com.salehhafyane.ecommerce.service.OrderQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Requires authentication; the user id comes from the JWT, not from query params.
@CrossOrigin
@RestController
@RequestMapping("/api/user/orders")
@RequiredArgsConstructor
public class UserOrderController {

    private final OrderQueryService orderQueryService;

    // GET /api/user/orders — the logged-in customer's order history, newest first.
    @GetMapping
    public List<OrderSummaryDTO> getMyOrders(@AuthenticationPrincipal User user) {
        return orderQueryService.getOrdersForUser(user.getId());
    }
}

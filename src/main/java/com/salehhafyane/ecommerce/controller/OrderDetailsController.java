package com.salehhafyane.ecommerce.controller;

import com.salehhafyane.ecommerce.dto.OrderDetailsDTO;
import com.salehhafyane.ecommerce.entity.User;
import com.salehhafyane.ecommerce.service.OrderQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderDetailsController {

    private final OrderQueryService orderQueryService;

    /** READ – full order details; owner or admin only (enforced in the service). */
    @GetMapping("/{id}")
    public OrderDetailsDTO getOrderDetails(@PathVariable Long id, @AuthenticationPrincipal User user) {
        return orderQueryService.getOrderDetails(id, user);
    }
}

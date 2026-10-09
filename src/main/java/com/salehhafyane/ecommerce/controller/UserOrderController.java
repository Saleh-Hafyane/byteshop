package com.salehhafyane.ecommerce.controller;

import com.salehhafyane.ecommerce.dto.OrderSummaryDTO;
import com.salehhafyane.ecommerce.entity.User;
import com.salehhafyane.ecommerce.service.OrderQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/user/orders")
@RequiredArgsConstructor
public class UserOrderController {

    private final OrderQueryService orderQueryService;

    /** LIST – orders of the currently logged-in user, newest first. */
    @GetMapping
    public List<OrderSummaryDTO> getMyOrders(@AuthenticationPrincipal User user) {
        return orderQueryService.getOrdersForUser(user.getId());
    }
}

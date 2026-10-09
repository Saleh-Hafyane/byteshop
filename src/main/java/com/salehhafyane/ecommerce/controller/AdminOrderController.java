package com.salehhafyane.ecommerce.controller;

import com.salehhafyane.ecommerce.dto.OrderSummaryDTO;
import com.salehhafyane.ecommerce.service.OrderQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
@CrossOrigin
public class AdminOrderController {

    private final OrderQueryService orderQueryService;

    /** READ – all orders in the system, newest first. */
    @GetMapping
    public List<OrderSummaryDTO> getAllOrders() {
        return orderQueryService.getAllOrders();
    }
}

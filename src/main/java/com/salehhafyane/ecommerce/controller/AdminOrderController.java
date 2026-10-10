package com.salehhafyane.ecommerce.controller;

import com.salehhafyane.ecommerce.dto.OrderStatusUpdateRequest;
import com.salehhafyane.ecommerce.dto.OrderSummaryDTO;
import com.salehhafyane.ecommerce.service.OrderAdminService;
import com.salehhafyane.ecommerce.service.OrderQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
@CrossOrigin
public class AdminOrderController {

    private final OrderQueryService orderQueryService;
    private final OrderAdminService orderAdminService;

    /** READ – all orders in the system, newest first. */
    @GetMapping
    public List<OrderSummaryDTO> getAllOrders() {
        return orderQueryService.getAllOrders();
    }

    /** UPDATE – transition an order to a new status; returns the updated summary. */
    @PatchMapping("/{id}/status")
    public OrderSummaryDTO updateStatus(@PathVariable Long id, @Valid @RequestBody OrderStatusUpdateRequest request) {
        return orderAdminService.updateStatus(id, request.getStatus());
    }
}

// Admin endpoints to list all orders and move one to a new status.
package com.salehhafyane.ecommerce.controller;

import com.salehhafyane.ecommerce.dto.OrderStatusUpdateRequest;
import com.salehhafyane.ecommerce.dto.OrderSummaryDTO;
import com.salehhafyane.ecommerce.service.OrderAdminService;
import com.salehhafyane.ecommerce.service.OrderQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Everything under /api/admin/orders is restricted to ROLE_ADMIN by the security config.
@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
@CrossOrigin
public class AdminOrderController {

    private final OrderQueryService orderQueryService;
    private final OrderAdminService orderAdminService;

    // GET /api/admin/orders — the manage-orders table, newest first.
    @GetMapping
    public List<OrderSummaryDTO> getAllOrders() {
        return orderQueryService.getAllOrders();
    }

    // PATCH /api/admin/orders/{id}/status — changes the status and returns the updated row.
    @PatchMapping("/{id}/status")
    public OrderSummaryDTO updateStatus(@PathVariable Long id, @Valid @RequestBody OrderStatusUpdateRequest request) {
        return orderAdminService.updateStatus(id, request.getStatus());
    }
}

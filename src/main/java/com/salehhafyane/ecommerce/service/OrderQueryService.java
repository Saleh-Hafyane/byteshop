// Contract for reading orders safely: own orders, admin orders, and guarded details.
package com.salehhafyane.ecommerce.service;

import com.salehhafyane.ecommerce.dto.OrderDetailsDTO;
import com.salehhafyane.ecommerce.dto.OrderSummaryDTO;
import com.salehhafyane.ecommerce.entity.User;

import java.util.List;

public interface OrderQueryService {

    // One customer's orders, newest first.
    List<OrderSummaryDTO> getOrdersForUser(Long userId);

    // Every order in the shop, newest first, admin only.
    List<OrderSummaryDTO> getAllOrders();

    // Full order details; throws 404 when missing, 403 when the requester is neither owner nor admin.
    OrderDetailsDTO getOrderDetails(Long orderId, User requester);
}

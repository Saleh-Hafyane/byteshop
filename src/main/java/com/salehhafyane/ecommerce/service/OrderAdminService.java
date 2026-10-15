// Contract for the admin order-status transition.
package com.salehhafyane.ecommerce.service;

import com.salehhafyane.ecommerce.dto.OrderSummaryDTO;
import com.salehhafyane.ecommerce.entity.OrderStatus;

public interface OrderAdminService {

    // Moves the order to the new status and returns its summary; throws 404 if the id is unknown.
    OrderSummaryDTO updateStatus(Long orderId, OrderStatus status);
}

package com.salehhafyane.ecommerce.service;

import com.salehhafyane.ecommerce.dto.OrderSummaryDTO;
import com.salehhafyane.ecommerce.entity.OrderStatus;

public interface OrderAdminService {

    /**
     * Transitions an order to a new status. Any transition is allowed.
     *
     * @param orderId   the order to update
     * @param status    the new status (never null: enforced by {@code @Valid} at the boundary)
     * @return the updated order as a summary
     * @throws com.salehhafyane.ecommerce.exceptions.OrderNotFoundException if the order does not exist
     */
    OrderSummaryDTO updateStatus(Long orderId, OrderStatus status);
}

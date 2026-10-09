package com.salehhafyane.ecommerce.service;

import com.salehhafyane.ecommerce.dto.OrderDetailsDTO;
import com.salehhafyane.ecommerce.dto.OrderSummaryDTO;
import com.salehhafyane.ecommerce.entity.User;

import java.util.List;

public interface OrderQueryService {

    /** Returns the orders of the given user, newest first. */
    List<OrderSummaryDTO> getOrdersForUser(Long userId);

    /** Returns all orders in the system, newest first. */
    List<OrderSummaryDTO> getAllOrders();

    /**
     * Returns the full details of an order.
     *
     * @param orderId   the order to load
     * @param requester the currently authenticated user
     * @throws com.salehhafyane.ecommerce.exceptions.OrderNotFoundException      if the order does not exist
     * @throws com.salehhafyane.ecommerce.exceptions.ForbiddenOperationException if the requester is neither the owner nor an admin
     */
    OrderDetailsDTO getOrderDetails(Long orderId, User requester);
}

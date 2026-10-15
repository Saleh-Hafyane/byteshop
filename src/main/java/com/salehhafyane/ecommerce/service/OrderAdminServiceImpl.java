// Admin-only order status changes, mapped to the summary row the table shows.
package com.salehhafyane.ecommerce.service;

import com.salehhafyane.ecommerce.dto.OrderMapper;
import com.salehhafyane.ecommerce.dto.OrderSummaryDTO;
import com.salehhafyane.ecommerce.entity.Order;
import com.salehhafyane.ecommerce.entity.OrderStatus;
import com.salehhafyane.ecommerce.exceptions.OrderNotFoundException;
import com.salehhafyane.ecommerce.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Transactional for the whole class; there's only one write method anyway.
@Service
@RequiredArgsConstructor
@Transactional
public class OrderAdminServiceImpl implements OrderAdminService {

    private final OrderRepository orderRepository;

    // Loads the order (or throws 404), sets the new status and returns the refreshed summary.
    @Override
    public OrderSummaryDTO updateStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        order.setStatus(status);
        // No explicit save(): the managed entity is flushed on commit.
        return OrderMapper.toSummary(order);
    }
}

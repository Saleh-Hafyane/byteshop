package com.salehhafyane.ecommerce.service;

import com.salehhafyane.ecommerce.dto.OrderDetailsDTO;
import com.salehhafyane.ecommerce.dto.OrderMapper;
import com.salehhafyane.ecommerce.dto.OrderSummaryDTO;
import com.salehhafyane.ecommerce.entity.Order;
import com.salehhafyane.ecommerce.entity.Role;
import com.salehhafyane.ecommerce.entity.User;
import com.salehhafyane.ecommerce.exceptions.ForbiddenOperationException;
import com.salehhafyane.ecommerce.exceptions.OrderNotFoundException;
import com.salehhafyane.ecommerce.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderQueryServiceImpl implements OrderQueryService {

    private final OrderRepository orderRepository;

    @Override
    public List<OrderSummaryDTO> getOrdersForUser(Long userId) {
        return orderRepository.findByUserIdOrderByDateCreatedDesc(userId).stream()
                .map(OrderMapper::toSummary)
                .toList();
    }

    @Override
    public List<OrderSummaryDTO> getAllOrders() {
        return orderRepository.findAll(Sort.by(Sort.Direction.DESC, "dateCreated")).stream()
                .map(OrderMapper::toSummary)
                .toList();
    }

    @Override
    public OrderDetailsDTO getOrderDetails(Long orderId, User requester) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        boolean isOwner = requester != null
                && order.getUser() != null
                && order.getUser().getId() != null
                && order.getUser().getId().equals(requester.getId());
        boolean isAdmin = requester != null && requester.getRole() == Role.ADMIN;
        if (!isOwner && !isAdmin) {
            throw new ForbiddenOperationException("You are not allowed to view this order");
        }
        return OrderMapper.toDetails(order);
    }
}

package com.salehhafyane.ecommerce.service;

import com.salehhafyane.ecommerce.dto.OrderSummaryDTO;
import com.salehhafyane.ecommerce.entity.Order;
import com.salehhafyane.ecommerce.entity.OrderStatus;
import com.salehhafyane.ecommerce.entity.User;
import com.salehhafyane.ecommerce.exceptions.OrderNotFoundException;
import com.salehhafyane.ecommerce.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class OrderAdminServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderAdminServiceImpl orderAdminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Order pendingOrder() {
        User owner = new User();
        owner.setId(1L);
        owner.setUsername("user1");
        Order order = Order.builder()
                .totalQuantity(1)
                .totalPrice(new BigDecimal("99.99"))
                .status(OrderStatus.PENDING)
                .build();
        order.setId(10L);
        order.setOrderTrackingNumber("track-10");
        order.setUser(owner);
        return order;
    }

    @Test
    void updateStatus_TransitionsAndReturnsSummary() {
        Order order = pendingOrder();
        when(orderRepository.findById(10L)).thenReturn(Optional.of(order));

        OrderSummaryDTO result = orderAdminService.updateStatus(10L, OrderStatus.SHIPPED);

        assertEquals(OrderStatus.SHIPPED, order.getStatus());
        assertEquals(10L, result.getId());
        assertEquals(OrderStatus.SHIPPED, result.getStatus());
        assertEquals("user1", result.getCustomerUsername());
        // Managed entity: flushed on commit, no explicit save() expected.
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void updateStatus_MissingOrderThrows404() {
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class,
                () -> orderAdminService.updateStatus(999L, OrderStatus.CANCELLED));
    }
}

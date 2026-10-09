package com.salehhafyane.ecommerce.service;

import com.salehhafyane.ecommerce.dto.OrderDetailsDTO;
import com.salehhafyane.ecommerce.dto.OrderSummaryDTO;
import com.salehhafyane.ecommerce.entity.*;
import com.salehhafyane.ecommerce.exceptions.ForbiddenOperationException;
import com.salehhafyane.ecommerce.exceptions.OrderNotFoundException;
import com.salehhafyane.ecommerce.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderQueryServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderQueryServiceImpl orderQueryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private User user(Long id, Role role) {
        User u = new User();
        u.setId(id);
        u.setUsername("user" + id);
        u.setRole(role);
        return u;
    }

    private Order order(Long id, User owner) {
        Address address = new Address();
        address.setId(1L);
        address.setCity("Rabat");
        address.setFullAddress("Avenue Mohammed V");
        OrderItem item = new OrderItem();
        item.setId(1L);
        item.setProductId(7L);
        item.setProductName("Laptop Pro");
        item.setQuantity(2);
        item.setUnitPrice(new BigDecimal("99.99"));
        Order o = Order.builder()
                .totalQuantity(2)
                .totalPrice(new BigDecimal("199.98"))
                .status(OrderStatus.PENDING)
                .build();
        o.setId(id);
        o.setOrderTrackingNumber("track-" + id);
        o.setDateCreated(new Date());
        o.setUser(owner);
        o.setAddress(address);
        o.add(item);
        return o;
    }

    @Test
    void getOrdersForUser_ReturnsSummariesForThatUser() {
        User owner = user(1L, Role.USER);
        Order o = order(10L, owner);
        when(orderRepository.findByUserIdOrderByDateCreatedDesc(1L)).thenReturn(List.of(o));

        List<OrderSummaryDTO> result = orderQueryService.getOrdersForUser(1L);

        assertEquals(1, result.size());
        OrderSummaryDTO summary = result.get(0);
        assertEquals(10L, summary.getId());
        assertEquals("track-10", summary.getOrderTrackingNumber());
        assertEquals(OrderStatus.PENDING, summary.getStatus());
        assertEquals("user1", summary.getCustomerUsername());
        verify(orderRepository).findByUserIdOrderByDateCreatedDesc(1L);
    }

    @Test
    void getAllOrders_SortedNewestFirst() {
        when(orderRepository.findAll(Sort.by(Sort.Direction.DESC, "dateCreated")))
                .thenReturn(List.of(order(2L, user(1L, Role.USER)), order(1L, user(2L, Role.USER))));

        List<OrderSummaryDTO> result = orderQueryService.getAllOrders();

        assertEquals(2, result.size());
        assertEquals(2L, result.get(0).getId());
        verify(orderRepository).findAll(Sort.by(Sort.Direction.DESC, "dateCreated"));
    }

    @Test
    void getOrderDetails_OwnerSeesFullDetails() {
        User owner = user(1L, Role.USER);
        when(orderRepository.findById(10L)).thenReturn(Optional.of(order(10L, owner)));

        OrderDetailsDTO details = orderQueryService.getOrderDetails(10L, owner);

        assertEquals(10L, details.getId());
        assertEquals("user1", details.getCustomerUsername());
        assertEquals("Rabat", details.getAddress().getCity());
        assertEquals("Avenue Mohammed V", details.getAddress().getFullAddress());
        assertEquals(1, details.getOrderItems().size());
        assertEquals("Laptop Pro", details.getOrderItems().get(0).getProductName());
    }

    @Test
    void getOrderDetails_AdminCanReadAnyOrder() {
        User owner = user(1L, Role.USER);
        User admin = user(99L, Role.ADMIN);
        when(orderRepository.findById(10L)).thenReturn(Optional.of(order(10L, owner)));

        OrderDetailsDTO details = orderQueryService.getOrderDetails(10L, admin);

        assertEquals("user1", details.getCustomerUsername());
    }

    @Test
    void getOrderDetails_NonOwnerDenied() {
        User owner = user(1L, Role.USER);
        User stranger = user(2L, Role.USER);
        when(orderRepository.findById(10L)).thenReturn(Optional.of(order(10L, owner)));

        assertThrows(ForbiddenOperationException.class,
                () -> orderQueryService.getOrderDetails(10L, stranger));
    }

    @Test
    void getOrderDetails_MissingOrderThrows404() {
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class,
                () -> orderQueryService.getOrderDetails(999L, user(1L, Role.USER)));
    }
}

package com.salehhafyane.ecommerce.service;

import com.salehhafyane.ecommerce.dto.Purchase;
import com.salehhafyane.ecommerce.dto.PurchaseResponse;
import com.salehhafyane.ecommerce.entity.*;
import com.salehhafyane.ecommerce.repository.AddressRepository;
import com.salehhafyane.ecommerce.repository.OrderRepository;
import com.salehhafyane.ecommerce.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CheckoutServiceImpTest {

    // Mocked dependencies
    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private AddressRepository addressRepository;

    // InjectMocks automatically injects the mocks into the class being tested
    @InjectMocks
    private CheckoutServiceImp checkoutService;

    @BeforeEach
    void setUp() {
        // Initializes the mocks before each test
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testMakeOrder() {
        // Arrange

        // Mock user object
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");

        // Mock address object
        Address address = new Address();
        address.setCity("Rabat");

        // Mock order object
        Order order = new Order();
        order.setOrderTrackingNumber(UUID.randomUUID().toString());

        // Mock order item
        OrderItem orderItem = new OrderItem();
        orderItem.setProductId(1L);

        // Adding the order item to a set
        Set<OrderItem> orderItems = new HashSet<>();
        orderItems.add(orderItem);

        // Creating a Purchase object
        Purchase purchase = new Purchase();
        purchase.setOrder(order);
        purchase.setOrderItems(orderItems);
        purchase.setAddress(address);

        // Mock saved address returned by addressRepository.save()
        Address savedAddress = new Address();
        savedAddress.setCity("Rabat");

        // Defining mock behavior for repository methods
        when(addressRepository.save(any(Address.class))).thenReturn(savedAddress);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        PurchaseResponse response = checkoutService.makeOrder(purchase, user);

        // Assert

        // Ensures the returned tracking number matches the generated one
        assertEquals(order.getOrderTrackingNumber(), response.getOrderTrackingNumber());

        // Capturing the saved order for validation
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository, times(1)).save(orderCaptor.capture());

        // Validating the captured order
        Order capturedOrder = orderCaptor.getValue();
        assertEquals(user, capturedOrder.getUser()); // Ensure the user is correctly associated
        assertEquals(savedAddress, capturedOrder.getAddress()); // Ensure the address is saved and associated
        assertEquals(orderItems.size(), capturedOrder.getOrderItems().size()); // Verify the number of order items
    }
}

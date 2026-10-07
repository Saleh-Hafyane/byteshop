package com.salehhafyane.ecommerce.service;

import com.salehhafyane.ecommerce.dto.AddressDTO;
import com.salehhafyane.ecommerce.dto.OrderDTO;
import com.salehhafyane.ecommerce.dto.OrderItemDTO;
import com.salehhafyane.ecommerce.dto.PurchaseRequest;
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

import java.math.BigDecimal;
import java.util.List;

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

        // Mock purchase request (flat DTOs, no entities cross the API boundary)
        AddressDTO addressDTO = AddressDTO.builder()
                .city("Rabat")
                .fullAddress("Avenue Mohammed V")
                .build();

        OrderDTO orderDTO = OrderDTO.builder()
                .totalQuantity(2)
                .totalPrice(new BigDecimal("199.98"))
                .build();

        OrderItemDTO itemDTO = OrderItemDTO.builder()
                .imageUrl("https://example.com/img.png")
                .unitPrice(new BigDecimal("99.99"))
                .quantity(2)
                .productId(1L)
                .build();

        // Creating a PurchaseRequest object
        PurchaseRequest purchase = PurchaseRequest.builder()
                .address(addressDTO)
                .order(orderDTO)
                .orderItems(List.of(itemDTO))
                .build();

        // Mock saved address returned by addressRepository.save()
        Address savedAddress = new Address();
        savedAddress.setCity("Rabat");
        savedAddress.setFullAddress("Avenue Mohammed V");

        // Defining mock behavior for repository methods
        when(addressRepository.save(any(Address.class))).thenReturn(savedAddress);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        PurchaseResponse response = checkoutService.makeOrder(purchase, user);

        // Assert

        // A tracking number must be generated server-side
        assertEquals(36, response.getOrderTrackingNumber().length());

        // Capturing the saved order for validation
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository, times(1)).save(orderCaptor.capture());

        // Validating the captured order
        Order capturedOrder = orderCaptor.getValue();
        assertEquals(user, capturedOrder.getUser()); // Ensure the user is correctly associated
        assertEquals(savedAddress, capturedOrder.getAddress()); // Ensure the address is saved and associated
        assertEquals(1, capturedOrder.getOrderItems().size()); // Verify the number of order items
        assertEquals(new BigDecimal("199.98"), capturedOrder.getTotalPrice());
        assertEquals(Integer.valueOf(2), capturedOrder.getTotalQuantity());
        assertEquals("PENDING", capturedOrder.getStatus()); // Status is server-generated
    }
}

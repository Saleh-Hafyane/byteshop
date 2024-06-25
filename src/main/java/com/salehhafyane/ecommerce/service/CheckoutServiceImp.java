package com.salehhafyane.ecommerce.service;

import com.salehhafyane.ecommerce.dto.Purchase;
import com.salehhafyane.ecommerce.dto.PurchaseResponse;
import com.salehhafyane.ecommerce.entity.Address;
import com.salehhafyane.ecommerce.entity.Order;
import com.salehhafyane.ecommerce.entity.OrderItem;
import com.salehhafyane.ecommerce.entity.User;
import com.salehhafyane.ecommerce.repository.AddressRepository;
import com.salehhafyane.ecommerce.repository.OrderRepository;
import com.salehhafyane.ecommerce.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
public class CheckoutServiceImp implements CheckoutService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    public CheckoutServiceImp(UserRepository userRepository, OrderRepository orderRepository,AddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    @Transactional
    public PurchaseResponse makeOrder(Purchase purchase, User user) {
        // Create a new order from the purchase
        Order order = purchase.getOrder();

        // Generate a unique order tracking number
        String orderTrackingNumber = generateOrderTrackingNumber();
        order.setOrderTrackingNumber(orderTrackingNumber);

        // Add order items to the order
        Set<OrderItem> orderItems = purchase.getOrderItems();
        orderItems.forEach(order::add);

        // Set the address for the order
        Address savedAddress = addressRepository.save(purchase.getAddress()); // Persist the address first
        order.setAddress(savedAddress);

        // Assign the user to the order
        order.setUser(user);

        // Save the order to the database
        orderRepository.save(order);

        // Return a response with the order tracking number
        return new PurchaseResponse(orderTrackingNumber);
    }

    // Helper method to generate a unique order tracking number
    private String generateOrderTrackingNumber() {
        return UUID.randomUUID().toString();
    }
}

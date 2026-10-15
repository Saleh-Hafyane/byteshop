// Turns the checkout request into saved rows: order, items and address in one transaction.
package com.salehhafyane.ecommerce.service;

import com.salehhafyane.ecommerce.dto.OrderItemDTO;
import com.salehhafyane.ecommerce.dto.OrderMapper;
import com.salehhafyane.ecommerce.dto.PurchaseRequest;
import com.salehhafyane.ecommerce.dto.PurchaseResponse;
import com.salehhafyane.ecommerce.entity.Address;
import com.salehhafyane.ecommerce.entity.Order;
import com.salehhafyane.ecommerce.entity.OrderItem;
import com.salehhafyane.ecommerce.entity.Product;
import com.salehhafyane.ecommerce.entity.User;
import com.salehhafyane.ecommerce.repository.AddressRepository;
import com.salehhafyane.ecommerce.repository.OrderRepository;
import com.salehhafyane.ecommerce.repository.ProductRepository;
import com.salehhafyane.ecommerce.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

// Client only sends data — prices, names, status and tracking number all come from here.
@Service
public class CheckoutServiceImp implements CheckoutService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;
    public CheckoutServiceImp(UserRepository userRepository, OrderRepository orderRepository,AddressRepository addressRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.addressRepository = addressRepository;
        this.productRepository = productRepository;
    }

    // One transaction: build order, verify products, save everything, return the tracking number.
    @Override
    @Transactional
    public PurchaseResponse makeOrder(PurchaseRequest purchase, User user) {
        // Build the order; status always starts PENDING and no tracking number is taken from the client.
        Order order = OrderMapper.toEntity(purchase.getOrder());

        // Fresh UUID so every order can be tracked with its own number.
        String orderTrackingNumber = generateOrderTrackingNumber();
        order.setOrderTrackingNumber(orderTrackingNumber);

        // Every line is validated against the real catalog before the order is saved.
        for (OrderItemDTO itemDTO : purchase.getOrderItems()) {
            OrderItem item = OrderMapper.toEntity(itemDTO);
            // Copy the current product name onto the line, and 400 if the product id no longer exists.
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found: " + itemDTO.getProductId()));
            item.setProductName(product.getName());
            order.add(item);
        }

        // Address gets its own row first because the order links to its id.
        Address savedAddress = addressRepository.save(OrderMapper.toEntity(purchase.getAddress()));
        order.setAddress(savedAddress);

        // The order belongs to the logged-in user, never to an id sent by the browser.
        order.setUser(user);

        // Cascade saves the order together with all its items.
        orderRepository.save(order);

        // The only value the confirmation page needs back.
        return new PurchaseResponse(orderTrackingNumber);
    }

    // Tracking numbers are plain random UUIDs.
    private String generateOrderTrackingNumber() {
        return UUID.randomUUID().toString();
    }
}

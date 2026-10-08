package com.salehhafyane.ecommerce.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Regression tests for the serialization hardening of JPA entities.
 * Guards against two classes of leaks fixed at the API boundary:
 * 1. The password hash leaving the backend inside a serialized {@link User}.
 * 2. Back-reference leaks / runaway recursion from bidirectional JPA relations.
 */
class EntitySerializationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private User buildUser() {
        return User.builder()
                .id(1L)
                .firstname("Jane")
                .lastname("Doe")
                .username("janedoe")
                .email("jane@example.com")
                .password("TopSecret!123")
                .role(Role.USER)
                .build();
    }

    private Order buildOrder() {
        return Order.builder()
                .id(10L)
                .orderTrackingNumber("8f14e45f-ceea-467f-a1d6-1f6ef4b7f8f2")
                .status("PENDING")
                .totalPrice(new BigDecimal("199.98"))
                .totalQuantity(2)
                .build();
    }

    @Test
    void passwordIsNeverSerialized() throws Exception {
        String json = objectMapper.writeValueAsString(buildUser());

        assertFalse(json.contains("\"password\""), "Serialized user must not expose the password field: " + json);
        assertFalse(json.contains("TopSecret!123"), "Serialized user must not expose the password value: " + json);
        assertTrue(json.contains("janedoe"), "User data itself must still serialize");
    }

    @Test
    void userOrderCycleSerializesWithoutLeaks() throws Exception {
        User user = buildUser();
        Order order = buildOrder();
        user.add(order); // establishes the bidirectional cycle user <-> order

        String orderJson = objectMapper.writeValueAsString(order);
        String userJson = objectMapper.writeValueAsString(user);

        // The cycle edge must not be serialized in either direction
        assertFalse(orderJson.contains("\"user\""), "Order must not leak its User: " + orderJson);
        assertFalse(userJson.contains("\"orders\""), "User must not leak its Orders: " + userJson);

        // Positive controls: order data itself must still serialize
        assertTrue(orderJson.contains("8f14e45f-ceea-467f-a1d6-1f6ef4b7f8f2"), "Tracking number must serialize");
        assertTrue(orderJson.contains("PENDING"), "Status must serialize");
    }

    @Test
    void orderGraphSerializesWithoutCycleLeaks() throws Exception {
        Order order = buildOrder();
        order.setUser(buildUser());

        Address address = new Address();
        address.setCity("Rabat");
        address.setFullAddress("Avenue Mohammed V");
        address.setOrder(order);
        order.setAddress(address);

        OrderItem item = new OrderItem();
        item.setImageUrl("https://example.com/img.png");
        item.setUnitPrice(new BigDecimal("99.99"));
        item.setQuantity(2);
        item.setProductId(1L);
        item.setOrder(order);
        order.add(item);

        String orderJson = objectMapper.writeValueAsString(order);
        assertFalse(orderJson.contains("\"user\""), "Order must not leak its User: " + orderJson);
        assertFalse(orderJson.contains("\"orderItems\""), "Order must not leak its OrderItems: " + orderJson);
        assertFalse(orderJson.contains("\"address\""), "Order must not leak its Address: " + orderJson);

        String addressJson = objectMapper.writeValueAsString(address);
        assertFalse(addressJson.contains("\"order\""), "Address must not leak its Order: " + addressJson);
        assertTrue(addressJson.contains("Rabat"), "Address data itself must still serialize");

        String itemJson = objectMapper.writeValueAsString(item);
        assertFalse(itemJson.contains("\"order\""), "OrderItem must not leak its Order: " + itemJson);
        assertTrue(itemJson.contains("productId"), "OrderItem data itself must still serialize");
    }

    @Test
    void productCategoryCycleSerializesWithoutLeaks() throws Exception {
        ProductCategory category = new ProductCategory();
        category.setId(3L);
        category.setCategoryName("Laptops");

        Product product = new Product();
        product.setId(7L);
        product.setName("ThinkPad");
        product.setCategory(category);
        category.setProducts(Set.of(product));

        String productJson = objectMapper.writeValueAsString(product);
        String categoryJson = objectMapper.writeValueAsString(category);

        assertFalse(productJson.contains("\"category\""), "Product must not leak its Category: " + productJson);
        assertFalse(categoryJson.contains("\"products\""), "Category must not leak its Products: " + categoryJson);

        // Positive controls: entity data itself must still serialize
        assertTrue(productJson.contains("ThinkPad"), "Product name must serialize");
        assertTrue(categoryJson.contains("Laptops"), "Category name must serialize");
    }
}

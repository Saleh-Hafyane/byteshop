package com.salehhafyane.ecommerce.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Bean-validation contract for the checkout request DTO: every constraint here
 * is a server-side guard that replaced trust in client-supplied entity JSON.
 */
class PurchaseRequestValidationTest {

    private static ValidatorFactory factory;
    private Validator validator;

    @BeforeAll
    static void createFactory() {
        factory = Validation.buildDefaultValidatorFactory();
    }

    @AfterAll
    static void closeFactory() {
        factory.close();
    }

    @BeforeEach
    void setUp() {
        validator = factory.getValidator();
    }

    private PurchaseRequest validPurchase() {
        AddressDTO address = AddressDTO.builder()
                .city("Rabat")
                .fullAddress("Avenue Mohammed V")
                .build();
        OrderDTO order = OrderDTO.builder()
                .totalQuantity(2)
                .totalPrice(new BigDecimal("199.98"))
                .build();
        OrderItemDTO item = OrderItemDTO.builder()
                .imageUrl("https://example.com/img.png")
                .unitPrice(new BigDecimal("99.99"))
                .quantity(2)
                .productId(1L)
                .build();
        return PurchaseRequest.builder()
                .address(address)
                .order(order)
                .orderItems(List.of(item))
                .build();
    }

    private String messageFor(PurchaseRequest request, String propertyPath) {
        return validator.validate(request).stream()
                .filter(v -> v.getPropertyPath().toString().equals(propertyPath))
                .map(ConstraintViolation::getMessage)
                .findFirst()
                .orElse(null);
    }

    private boolean hasViolation(PurchaseRequest request, String propertyPath) {
        return validator.validate(request).stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals(propertyPath));
    }

    @Test
    void validPurchaseHasNoViolations() {
        assertTrue(validator.validate(validPurchase()).isEmpty(), "A valid purchase must produce no violations");
    }

    @Test
    void missingAddressIsRejected() {
        PurchaseRequest request = validPurchase();
        request.setAddress(null);

        assertEquals("Address is required", messageFor(request, "address"));
    }

    @Test
    void missingOrderIsRejected() {
        PurchaseRequest request = validPurchase();
        request.setOrder(null);

        assertEquals("Order is required", messageFor(request, "order"));
    }

    @Test
    void emptyOrderItemsAreRejected() {
        PurchaseRequest request = validPurchase();
        request.setOrderItems(List.of());

        assertEquals("Order must contain at least one item", messageFor(request, "orderItems"));
    }

    @Test
    void blankCityIsRejected() {
        PurchaseRequest request = validPurchase();
        request.getAddress().setCity("   ");

        assertEquals("City is required", messageFor(request, "address.city"));
    }

    @Test
    void blankFullAddressIsRejected() {
        PurchaseRequest request = validPurchase();
        request.getAddress().setFullAddress("");

        assertEquals("Full address is required", messageFor(request, "address.fullAddress"));
    }

    @Test
    void missingTotalQuantityIsRejected() {
        PurchaseRequest request = validPurchase();
        request.getOrder().setTotalQuantity(null);

        assertEquals("Total quantity is required", messageFor(request, "order.totalQuantity"));
    }

    @Test
    void negativeTotalPriceIsRejected() {
        PurchaseRequest request = validPurchase();
        request.getOrder().setTotalPrice(new BigDecimal("-1.00"));

        assertEquals("Total price must be zero or positive", messageFor(request, "order.totalPrice"));
    }

    @Test
    void missingUnitPriceIsRejected() {
        PurchaseRequest request = validPurchase();
        request.getOrderItems().get(0).setUnitPrice(null);

        assertTrue(hasViolation(request, "orderItems[0].unitPrice"));
    }

    @Test
    void quantityBelowOneIsRejected() {
        PurchaseRequest request = validPurchase();
        request.getOrderItems().get(0).setQuantity(0);

        assertEquals("Quantity must be at least 1", messageFor(request, "orderItems[0].quantity"));
    }

    @Test
    void missingProductIdIsRejected() {
        PurchaseRequest request = validPurchase();
        request.getOrderItems().get(0).setProductId(null);

        assertEquals("Product id is required", messageFor(request, "orderItems[0].productId"));
    }

    @Test
    void requestExposesOnlyAddressOrderAndItems() {
        // The request must not offer any field the client could use to influence
        // server-owned order state (status / tracking number live on the entity only).
        Set<String> fields = java.util.Arrays.stream(PurchaseRequest.class.getDeclaredFields())
                .map(java.lang.reflect.Field::getName)
                .collect(Collectors.toSet());

        assertEquals(Set.of("address", "order", "orderItems"), fields,
                "PurchaseRequest must expose exactly the three client-supplied sections");
        assertNull(messageFor(validPurchase(), "status"));
        assertNull(messageFor(validPurchase(), "orderTrackingNumber"));
    }
}

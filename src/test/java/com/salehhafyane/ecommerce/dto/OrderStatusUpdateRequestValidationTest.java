package com.salehhafyane.ecommerce.dto;

import com.salehhafyane.ecommerce.entity.OrderStatus;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Bean-validation contract for the admin status-transition request: the
 * {@code @NotNull} guard rejects payloads that omit the status field entirely.
 * Unknown enum values fail earlier at deserialization (also mapped to 400) —
 * covered end-to-end in OrderWorkflowIntegrationTest.
 */
class OrderStatusUpdateRequestValidationTest {

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

    private String messageFor(OrderStatusUpdateRequest request, String propertyPath) {
        return validator.validate(request).stream()
                .filter(v -> v.getPropertyPath().toString().equals(propertyPath))
                .map(ConstraintViolation::getMessage)
                .findFirst()
                .orElse(null);
    }

    @Test
    void validStatusHasNoViolations() {
        OrderStatusUpdateRequest request = OrderStatusUpdateRequest.builder()
                .status(OrderStatus.SHIPPED)
                .build();

        Set<ConstraintViolation<OrderStatusUpdateRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty(), "A known status must produce no violations");
    }

    @Test
    void missingStatusIsRejected() {
        OrderStatusUpdateRequest request = new OrderStatusUpdateRequest();

        assertEquals("Status is required", messageFor(request, "status"));
    }
}
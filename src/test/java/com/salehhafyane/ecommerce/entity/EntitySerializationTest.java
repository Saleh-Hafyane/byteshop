// Guards the serialization hardening: passwords must never appear in API JSON.
package com.salehhafyane.ecommerce.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Serializes real entities with Jackson and inspects the raw JSON output.
class EntitySerializationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Account with a deliberately obvious password to hunt for in the JSON.
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
                .status(OrderStatus.PENDING)
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
}

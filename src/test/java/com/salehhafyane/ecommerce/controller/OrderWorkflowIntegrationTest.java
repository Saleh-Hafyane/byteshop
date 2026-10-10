package com.salehhafyane.ecommerce.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salehhafyane.ecommerce.entity.Role;
import com.salehhafyane.ecommerce.entity.User;
import com.salehhafyane.ecommerce.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Guards for the order workflow read side (chapter 7):
 * security rules, ownership enforcement, and DTO shapes on the wire.
 */
@SpringBootTest
@AutoConfigureMockMvc
class OrderWorkflowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ---------- helpers ----------

    private String registerUser() throws Exception {
        String unique = UUID.randomUUID().toString().substring(0, 8);
        String body = objectMapper.writeValueAsString(Map.of(
                "firstname", "Test",
                "lastname", "User",
                "username", "user-" + unique,
                "email", "user-" + unique + "@example.com",
                "password", "test1234"
        ));
        MvcResult result = mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString(StandardCharsets.UTF_8));
        return json.get("token").asText();
    }

    /**
     * Self-sufficient admin login: creates a dedicated ADMIN user directly so
     * the test never depends on the seeded admin row's password state.
     */
    private String adminToken() throws Exception {
        String unique = UUID.randomUUID().toString().substring(0, 8);
        User admin = User.builder()
                .firstname("Admin")
                .lastname("Test")
                .username("admin-" + unique)
                .email("admin-" + unique + "@example.com")
                .password(passwordEncoder.encode("admin1234"))
                .role(Role.ADMIN)
                .build();
        userRepository.save(admin);
        String body = objectMapper.writeValueAsString(Map.of(
                "username", admin.getUsername(),
                "password", "admin1234"
        ));
        MvcResult result = mockMvc.perform(post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString(StandardCharsets.UTF_8));
        return json.get("token").asText();
    }

    private String purchaseJson(long productId) {
        return """
                {
                  "address": {"city": "Rabat", "fullAddress": "Avenue Mohammed V"},
                  "order": {"totalQuantity": 1, "totalPrice": 99.99},
                  "orderItems": [{
                    "imageUrl": "https://example.com/img.png",
                    "unitPrice": 99.99,
                    "quantity": 1,
                    "productId": %d
                  }]
                }
                """.formatted(productId);
    }

    private void purchase(String token, long productId) throws Exception {
        mockMvc.perform(post("/api/checkout/purchase")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(purchaseJson(productId)))
                .andExpect(status().isOk());
    }

    private String newestOrderId(String token) throws Exception {
        MvcResult result = mockMvc.perform(get("/api/user/orders")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode orders = objectMapper.readTree(result.getResponse().getContentAsString(StandardCharsets.UTF_8));
        return orders.get(0).get("id").asText();
    }

    // ---------- security rules ----------

    @Test
    void orderEndpointsRequireAuthentication() throws Exception {
        mockMvc.perform(get("/api/user/orders"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(get("/api/admin/orders"))
                .andExpect(status().isUnauthorized());
    }

    // ---------- user history ----------

    @Test
    void userSeesOnlyOwnOrders() throws Exception {
        String tokenA = registerUser();
        String tokenB = registerUser();
        purchase(tokenA, 1L);

        MvcResult resultA = mockMvc.perform(get("/api/user/orders")
                        .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode ordersA = objectMapper.readTree(resultA.getResponse().getContentAsString(StandardCharsets.UTF_8));
        assertTrue(ordersA.size() >= 1, "User A must see at least the order just placed");
        for (JsonNode row : ordersA) {
            assertFalse(row.get("customerUsername").asText().isBlank());
            assertFalse(row.has("user"), "Summary must not embed the user entity");
        }

        MvcResult resultB = mockMvc.perform(get("/api/user/orders")
                        .header("Authorization", "Bearer " + tokenB))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode ordersB = objectMapper.readTree(resultB.getResponse().getContentAsString(StandardCharsets.UTF_8));
        String trackingA = ordersA.get(0).get("orderTrackingNumber").asText();
        for (JsonNode row : ordersB) {
            assertNotEquals(trackingA, row.get("orderTrackingNumber").asText(),
                    "User B must not see user A's order");
        }
    }

    // ---------- details + ownership ----------

    @Test
    void ownerReadsDetails_NonOwnerForbidden_AdminAllowed() throws Exception {
        String tokenA = registerUser();
        String tokenB = registerUser();
        purchase(tokenA, 1L);
        String orderId = newestOrderId(tokenA);

        // Owner: full details with address + items incl. product name snapshot
        mockMvc.perform(get("/api/orders/" + orderId)
                        .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.address.city").value("Rabat"))
                .andExpect(jsonPath("$.address.fullAddress").value("Avenue Mohammed V"))
                .andExpect(jsonPath("$.orderItems[0].productId").value(1))
                .andExpect(jsonPath("$.orderItems[0].productName").isString());

        // Non-owner: 403
        mockMvc.perform(get("/api/orders/" + orderId)
                        .header("Authorization", "Bearer " + tokenB))
                .andExpect(status().isForbidden());

        // Admin: allowed
        mockMvc.perform(get("/api/orders/" + orderId)
                        .header("Authorization", "Bearer " + adminToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber());
    }

    @Test
    void missingOrderReturns404() throws Exception {
        String token = registerUser();

        mockMvc.perform(get("/api/orders/999999")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }


    @Test
    void adminOrderListRequiresAdminRole() throws Exception {
        String userToken = registerUser();

        mockMvc.perform(get("/api/admin/orders")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/api/admin/orders")
                        .header("Authorization", "Bearer " + adminToken()))
                .andExpect(status().isOk());
    }

    // ---------- status transitions (chapter 8) ----------

    @Test
    void adminCanTransitionStatus_EndToEnd() throws Exception {
        String userToken = registerUser();
        String admin = adminToken();
        purchase(userToken, 1L);
        String orderId = newestOrderId(userToken);

        // Transition PENDING -> SHIPPED; response carries the updated summary
        mockMvc.perform(patch("/api/admin/orders/" + orderId + "/status")
                        .header("Authorization", "Bearer " + admin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"SHIPPED\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.status").value("SHIPPED"))
                .andExpect(jsonPath("$.customerUsername").isString());

        // Persistence proof through the real stack: details reflect the change
        mockMvc.perform(get("/api/orders/" + orderId)
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SHIPPED"));
    }

    @Test
    void statusTransition_MissingOrderReturns404() throws Exception {
        mockMvc.perform(patch("/api/admin/orders/999999/status")
                        .header("Authorization", "Bearer " + adminToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"CANCELLED\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void statusTransition_RejectsBadPayloadsWith400() throws Exception {
        String admin = adminToken();

        // Unknown enum value fails at deserialization ...
        mockMvc.perform(patch("/api/admin/orders/1/status")
                        .header("Authorization", "Bearer " + admin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"FLY_TO_MARS\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Malformed request body"));

        // ... and a missing status fails bean validation with field errors
        mockMvc.perform(patch("/api/admin/orders/1/status")
                        .header("Authorization", "Bearer " + admin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.status").value("Status is required"));
    }

    @Test
    void statusTransition_RequiresAdminRole() throws Exception {
        String userToken = registerUser();
        purchase(userToken, 1L);
        String orderId = newestOrderId(userToken);

        // Anonymous: 401
        mockMvc.perform(patch("/api/admin/orders/" + orderId + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"SHIPPED\"}"))
                .andExpect(status().isUnauthorized());

        // Non-admin: 403
        mockMvc.perform(patch("/api/admin/orders/" + orderId + "/status")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"SHIPPED\"}"))
                .andExpect(status().isForbidden());
    }
}

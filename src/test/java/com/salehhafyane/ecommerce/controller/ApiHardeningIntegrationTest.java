package com.salehhafyane.ecommerce.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * End-to-end guards for the API hardening checkpoints:
 * - sensitive repositories are not auto-exposed (checkpoint 4)
 * - Spring Data REST stays read-only for catalog resources (checkpoint 4)
 * - checkout requires authentication and validates its DTO (checkpoint 3)
 * - no password material leaves the API (checkpoint 1)
 */
@SpringBootTest
@AutoConfigureMockMvc
class ApiHardeningIntegrationTest {

    private static final String ANGULAR_ORIGIN = "http://localhost:4200";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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

    private String purchaseJson(String city, long productId) {
        // Deliberately includes server-owned fields that must be ignored
        return """
                {
                  "address": {"city": "%s", "fullAddress": "Avenue Mohammed V"},
                  "order": {"totalQuantity": 1, "totalPrice": 99.99, "status": "SHIPPED"},
                  "orderItems": [{
                    "imageUrl": "https://example.com/p.png",
                    "unitPrice": 99.99,
                    "quantity": 1,
                    "productId": %d
                  }],
                  "orderTrackingNumber": "hacked-by-client"
                }
                """.formatted(city, productId);
    }

    // ---------- SDR exposure (checkpoint 4) ----------

    @Test
    void sensitiveRepositoriesAreNotExposed() throws Exception {
        mockMvc.perform(get("/api/users")).andExpect(status().isNotFound());
        mockMvc.perform(get("/api/orders")).andExpect(status().isNotFound());
        mockMvc.perform(get("/api/addresses")).andExpect(status().isNotFound());
    }

    @Test
    void catalogReadsRemainAvailable() throws Exception {
        mockMvc.perform(get("/api/products")).andExpect(status().isOk());
        mockMvc.perform(get("/api/product-category")).andExpect(status().isOk());
        mockMvc.perform(get("/api/city")).andExpect(status().isOk());
    }

    @Test
    void dataRestWritesAreDisabled() throws Exception {
        mockMvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isMethodNotAllowed());
        mockMvc.perform(post("/api/city").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isMethodNotAllowed());
        mockMvc.perform(post("/api/product-category").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isMethodNotAllowed());
        mockMvc.perform(put("/api/products/1").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isMethodNotAllowed());
        mockMvc.perform(patch("/api/products/1").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isMethodNotAllowed());
        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void corsPreflightAllowsAngularDevOrigin() throws Exception {
        mockMvc.perform(options("/api/products")
                        .header("Origin", ANGULAR_ORIGIN)
                        .header("Access-Control-Request-Method", "GET"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", ANGULAR_ORIGIN));
    }

    // ---------- auth / authorization ----------

    @Test
    void checkoutRequiresAuthentication() throws Exception {
        mockMvc.perform(post("/api/checkout/purchase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(purchaseJson("Rabat", 1L)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void adminEndpointsRequireAdminRole() throws Exception {
        mockMvc.perform(get("/api/admin/products"))
                .andExpect(status().isUnauthorized());
    }

    // ---------- secrets (checkpoint 1) ----------

    @Test
    void productResponsesDoNotLeakPasswordMaterial() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andReturn();
        String body = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        assertFalse(body.contains("password"), "Products response must not contain password material");
    }

    // ---------- DTO validation boundary (checkpoint 3) ----------

    @Test
    void checkoutRejectsInvalidPayloadWithFieldErrors() throws Exception {
        String token = registerUser();

        mockMvc.perform(post("/api/checkout/purchase")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(purchaseJson("   ", 1L)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation Failed"))
                .andExpect(jsonPath("$.errors['address.city']").value("City is required"));
    }

    @Test
    void checkoutReturnsServerGeneratedTrackingNumberEndToEnd() throws Exception {
        String token = registerUser();

        MvcResult result = mockMvc.perform(post("/api/checkout/purchase")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(purchaseJson("Rabat", 1L)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderTrackingNumber").isString())
                .andReturn();

        String body = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        String trackingNumber = objectMapper.readTree(body).get("orderTrackingNumber").asText();

        assertEquals(36, trackingNumber.length(), "Tracking number must be a server-generated UUID");
        assertNotEquals("hacked-by-client", trackingNumber, "Client-supplied tracking number must be ignored");
        assertDoesNotThrow(() -> UUID.fromString(trackingNumber), "Tracking number must be a valid UUID");
    }
}

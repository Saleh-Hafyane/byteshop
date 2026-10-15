// Mapping contract for product DTOs: the category flattens to plain id/name, never a nested entity.
package com.salehhafyane.ecommerce.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.salehhafyane.ecommerce.entity.Product;
import com.salehhafyane.ecommerce.entity.ProductCategory;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Serializes the DTO as well to prove no entity graph leaks into the JSON.
class ProductMapperTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Standard product wired to whatever category the test wants to exercise.
    private Product buildProduct(ProductCategory category) {
        Product product = new Product();
        product.setId(7L);
        product.setName("ThinkPad");
        product.setDescription("Business laptop");
        product.setUnitPrice(new BigDecimal("1299.00"));
        product.setImageUrl("https://example.com/thinkpad.png");
        product.setUnitsInStock(12);
        product.setCategory(category);
        return product;
    }

    // Category must arrive as categoryId + categoryName scalars with no nested object in the JSON.
    @Test
    void productWithCategoryMapsFlatCategoryIdAndName() throws Exception {
        ProductCategory category = new ProductCategory();
        category.setId(3L);
        category.setCategoryName("Laptops");

        ProductDTO dto = ProductMapper.toDto(buildProduct(category));

        assertNotNull(dto);
        assertEquals(7L, dto.getId());
        assertEquals("ThinkPad", dto.getName());
        assertEquals("Business laptop", dto.getDescription());
        assertEquals(new BigDecimal("1299.00"), dto.getUnitPrice());
        assertEquals(3L, dto.getCategoryId());
        assertEquals("Laptops", dto.getCategoryName());

        // Serialize to catch any hidden nested category field.
        String json = objectMapper.writeValueAsString(dto);
        assertTrue(json.contains("categoryId"), "Flat categoryId must serialize");
        assertFalse(json.contains("\"category\":"), "ProductDTO must not serialize a nested category: " + json);
    }

    // Category entity maps to its small DTO, and a null input maps to null instead of throwing.
    @Test
    void categoryMapsToDto() {
        ProductCategory category = new ProductCategory();
        category.setId(5L);
        category.setCategoryName("Phones");

        ProductCategoryDTO dto = ProductMapper.toDto(category);

        assertNotNull(dto);
        assertEquals(5L, dto.getId());
        assertEquals("Phones", dto.getCategoryName());
        assertNull(ProductMapper.toDto((ProductCategory) null));
    }
}

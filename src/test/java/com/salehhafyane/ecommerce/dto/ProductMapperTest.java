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

/**
 * Mapping contract for product DTOs: category information must be flattened
 * to id/name scalars so no entity graph crosses the API boundary.
 */
class ProductMapperTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

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

        // The DTO must stay flat: no nested category object crosses the boundary
        String json = objectMapper.writeValueAsString(dto);
        assertTrue(json.contains("categoryId"), "Flat categoryId must serialize");
        assertFalse(json.contains("\"category\":"), "ProductDTO must not serialize a nested category: " + json);
    }

    @Test
    void productWithoutCategoryMapsNullCategoryFields() {
        ProductDTO dto = ProductMapper.toDto(buildProduct(null));

        assertNotNull(dto);
        assertNull(dto.getCategoryId());
        assertNull(dto.getCategoryName());
    }

    @Test
    void nullProductReturnsNull() {
        assertNull(ProductMapper.toDto((Product) null));
    }

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

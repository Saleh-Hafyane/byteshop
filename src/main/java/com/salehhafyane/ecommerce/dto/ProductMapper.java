// Converts product entities to flat DTOs, flattening the category into two plain fields.
package com.salehhafyane.ecommerce.dto;

import com.salehhafyane.ecommerce.entity.Product;
import com.salehhafyane.ecommerce.entity.ProductCategory;
import lombok.NoArgsConstructor;

// Static utility class only, so the constructor is private.
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class ProductMapper {

    // Product to DTO; a missing category just becomes null id/name instead of crashing.
    public static ProductDTO toDto(Product product) {
        if (product == null) {
            return null;
        }
        ProductCategory category = product.getCategory();
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .unitPrice(product.getUnitPrice())
                .imageUrl(product.getImageUrl())
                .unitsInStock(product.getUnitsInStock())
                .dateCreated(product.getDateCreated())
                .lastUpdated(product.getLastUpdated())
                .categoryId(category != null ? category.getId() : null)
                .categoryName(category != null ? category.getCategoryName() : null)
                .build();
    }

    // Category entity to its small id + name DTO.
    public static ProductCategoryDTO toDto(ProductCategory category) {
        if (category == null) {
            return null;
        }
        return ProductCategoryDTO.builder()
                .id(category.getId())
                .categoryName(category.getCategoryName())
                .build();
    }
}

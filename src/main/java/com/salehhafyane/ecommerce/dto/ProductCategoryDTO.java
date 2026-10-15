// Category id + label, as sent to the admin category management page.
package com.salehhafyane.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Flat JSON version of ProductCategory.
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryDTO {
    private long id; // category id used in product queries
    private String categoryName; // display label like "Laptops"
}

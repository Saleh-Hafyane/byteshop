// Simple entity-level service used by the older category controller endpoints.
package com.salehhafyane.ecommerce.service;

import com.salehhafyane.ecommerce.entity.ProductCategory;
import com.salehhafyane.ecommerce.repository.ProductCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Works directly with ProductCategory entities rather than DTOs.
@Service
public class ProductCategoryService {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    // Insert a brand-new category row.
    public ProductCategory addCategory(ProductCategory category) {
        return productCategoryRepository.save(category);
    }
    // Delete a category by id (cascades to its products).
    public void removeCategory(Long id) {
        productCategoryRepository.deleteById(id);
    }
}

package com.salehhafyane.ecommerce.service;

import com.salehhafyane.ecommerce.entity.ProductCategory;
import com.salehhafyane.ecommerce.repository.ProductCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductCategoryService {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    // Add a new category
    public ProductCategory addCategory(ProductCategory category) {
        return productCategoryRepository.save(category);
    }
    // remove a category
    public void removeCategory(Long id) {
        productCategoryRepository.deleteById(id);
    }
}

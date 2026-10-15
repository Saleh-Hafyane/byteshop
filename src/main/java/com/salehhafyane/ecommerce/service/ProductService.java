// Simple entity-level service used by the older product controller endpoints.
package com.salehhafyane.ecommerce.service;

import com.salehhafyane.ecommerce.entity.Product;
import com.salehhafyane.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

// Works directly with Product entities rather than DTOs.
@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    // Insert a brand-new product row.
    public void save(Product product) {
        productRepository.save(product);
    }
    // Delete a product by id.
    public void delete(Long id){
        productRepository.deleteById(id);
    }
    // Replace the editable fields of an existing product; 500 if the id doesn't exist.
    public void update(Long id, Product updatedProduct){
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        product.setName(updatedProduct.getName());
        product.setUnitPrice(updatedProduct.getUnitPrice());
        product.setCategory(updatedProduct.getCategory());
        product.setUnitsInStock(updatedProduct.getUnitsInStock());
        product.setDescription(updatedProduct.getDescription());
        product.setImageUrl(updatedProduct.getImageUrl());
        productRepository.save(product);
    }

}

package com.salehhafyane.ecommerce.service;

import com.salehhafyane.ecommerce.entity.Product;
import com.salehhafyane.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public void save(Product product) {
        productRepository.save(product);
    }
    public void delete(Long id){
        productRepository.deleteById(id);
    }
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

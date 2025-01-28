package com.salehhafyane.ecommerce.controller;

import com.salehhafyane.ecommerce.entity.Product;
import com.salehhafyane.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
@CrossOrigin
public class AdminProductController {
    @Autowired
    private ProductService productService;

    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@RequestBody Product product) {
        productService.save(product);
        return ResponseEntity.ok().body(Map.of("message", "Product added successfully!"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        productService.update(id, product);
        return ResponseEntity.ok().body(Map.of("message", "Product updated successfully!"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.ok().body(Map.of("message", "Product deleted successfully!"));
    }

}

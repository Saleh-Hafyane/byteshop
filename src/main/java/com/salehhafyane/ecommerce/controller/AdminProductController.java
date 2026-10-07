package com.salehhafyane.ecommerce.controller;

import com.salehhafyane.ecommerce.dto.ProductDTO;
import com.salehhafyane.ecommerce.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
@CrossOrigin
public class AdminProductController {

    private final IProductService productService;

    /** CREATE */
    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@RequestBody ProductDTO product) {
        productService.save(product);
        return ResponseEntity.ok().body(Map.of("message", "Product added successfully!"));
    }

    /** READ – all products */
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAll());
    }

    /** READ – single product */
    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Long id) {
        return productService.getById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** UPDATE */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody ProductDTO product) {
        productService.update(id, product);
        return ResponseEntity.ok().body(Map.of("message", "Product updated successfully!"));
    }

    /** DELETE */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.ok().body(Map.of("message", "Product deleted successfully!"));
    }
}

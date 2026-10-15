// Admin CRUD endpoints for products, used by the add/edit product pages.
package com.salehhafyane.ecommerce.controller;

import com.salehhafyane.ecommerce.dto.ProductDTO;
import com.salehhafyane.ecommerce.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

// Everything under /api/admin/products is restricted to ROLE_ADMIN by the security config.
@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
@CrossOrigin
public class AdminProductController {

    private final IProductService productService;

    // POST /api/admin/products/add — inserts a new catalog entry.
    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@RequestBody ProductDTO product) {
        productService.save(product);
        return ResponseEntity.ok().body(Map.of("message", "Product added successfully!"));
    }

    // GET /api/admin/products — the management table's data source.
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAll());
    }

    // GET /api/admin/products/{id} — one product for the edit form, 404 when unknown.
    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Long id) {
        return productService.getById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT /api/admin/products/{id} — overwrites the product's editable fields.
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody ProductDTO product) {
        productService.update(id, product);
        return ResponseEntity.ok().body(Map.of("message", "Product updated successfully!"));
    }

    // DELETE /api/admin/products/{id} — removes the product from the catalog.
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.ok().body(Map.of("message", "Product deleted successfully!"));
    }
}

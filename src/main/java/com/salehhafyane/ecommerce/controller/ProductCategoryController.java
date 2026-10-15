// CRUD endpoints for product categories, powering the admin category page.
package com.salehhafyane.ecommerce.controller;

import com.salehhafyane.ecommerce.dto.ProductCategoryDTO;
import com.salehhafyane.ecommerce.service.IProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Public for now — categories contain no sensitive data.
@RestController
@RequestMapping("/api/product-category")
@CrossOrigin
public class ProductCategoryController {

    @Autowired
    private IProductCategoryService categoryService;

    // POST /api/product-category/add — creates a category, returns 201.
    @PostMapping("/add")
    public ResponseEntity<ProductCategoryDTO> addCategory(@RequestBody ProductCategoryDTO category) {
        ProductCategoryDTO newCategory = categoryService.addCategory(category);
        return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
    }

    // GET /api/product-category/all — full list for dropdowns and the admin table.
    @GetMapping("/all")
    public ResponseEntity<List<ProductCategoryDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAll());
    }

    // GET /api/product-category/{id} — one category, 404 when unknown.
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        return categoryService.getById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT /api/product-category/update/{id} — renames an existing category.
    @PutMapping("/update/{id}")
    public ResponseEntity<ProductCategoryDTO> updateCategory(@PathVariable Long id,
                                                          @RequestBody ProductCategoryDTO category) {
        ProductCategoryDTO updated = categoryService.updateCategory(id, category);
        return ResponseEntity.ok(updated);
    }

    // DELETE /api/product-category/remove/{id} — removes the category, 200 with empty body.
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<?> removeCategory(@PathVariable Long id) {
        categoryService.removeCategory(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

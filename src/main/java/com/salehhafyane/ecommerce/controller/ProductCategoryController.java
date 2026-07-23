package com.salehhafyane.ecommerce.controller;

import com.salehhafyane.ecommerce.entity.ProductCategory;
import com.salehhafyane.ecommerce.service.IProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-category")
@CrossOrigin
public class ProductCategoryController {

    @Autowired
    private IProductCategoryService categoryService;

    /** CREATE */
    @PostMapping("/add")
    public ResponseEntity<ProductCategory> addCategory(@RequestBody ProductCategory category) {
        ProductCategory newCategory = categoryService.addCategory(category);
        return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
    }

    /** READ – all categories */
    @GetMapping("/all")
    public ResponseEntity<List<ProductCategory>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAll());
    }

    /** READ – single category */
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        return categoryService.getById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** UPDATE */
    @PutMapping("/update/{id}")
    public ResponseEntity<ProductCategory> updateCategory(@PathVariable Long id,
                                                          @RequestBody ProductCategory category) {
        ProductCategory updated = categoryService.updateCategory(id, category);
        return ResponseEntity.ok(updated);
    }

    /** DELETE */
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<?> removeCategory(@PathVariable Long id) {
        categoryService.removeCategory(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

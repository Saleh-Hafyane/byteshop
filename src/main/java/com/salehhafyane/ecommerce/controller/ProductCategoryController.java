package com.salehhafyane.ecommerce.controller;

import com.salehhafyane.ecommerce.entity.ProductCategory;
import com.salehhafyane.ecommerce.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product-category")
@CrossOrigin
public class ProductCategoryController {
    @Autowired
    private ProductCategoryService categoryService;

    // Add a new category
    @PostMapping("/add")
    public ResponseEntity<ProductCategory> addCategory(@RequestBody ProductCategory category) {
        ProductCategory newCategory = categoryService.addCategory(category);
        return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
    }
    // Remove a category
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<?> removeCategory(@PathVariable Long id) {
        categoryService.removeCategory(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

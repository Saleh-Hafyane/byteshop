package com.salehhafyane.ecommerce.service;

import com.salehhafyane.ecommerce.entity.ProductCategory;

import java.util.List;
import java.util.Optional;

public interface IProductCategoryService {
    ProductCategory addCategory(ProductCategory category);
    void removeCategory(Long id);
    ProductCategory updateCategory(Long id, ProductCategory updatedCategory);
    List<ProductCategory> getAll();
    Optional<ProductCategory> getById(Long id);
}

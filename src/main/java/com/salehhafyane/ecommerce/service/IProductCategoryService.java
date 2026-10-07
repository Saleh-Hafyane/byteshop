package com.salehhafyane.ecommerce.service;

import com.salehhafyane.ecommerce.dto.ProductCategoryDTO;

import java.util.List;
import java.util.Optional;

public interface IProductCategoryService {
    ProductCategoryDTO addCategory(ProductCategoryDTO category);
    void removeCategory(Long id);
    ProductCategoryDTO updateCategory(Long id, ProductCategoryDTO updatedCategory);
    List<ProductCategoryDTO> getAll();
    Optional<ProductCategoryDTO> getById(Long id);
}

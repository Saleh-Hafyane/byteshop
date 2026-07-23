package com.salehhafyane.ecommerce.service;

import com.salehhafyane.ecommerce.entity.ProductCategory;
import com.salehhafyane.ecommerce.repository.ProductCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductCategoryServiceImpl implements IProductCategoryService {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Override
    public ProductCategory addCategory(ProductCategory category) {
        return productCategoryRepository.save(category);
    }

    @Override
    public void removeCategory(Long id) {
        productCategoryRepository.deleteById(id);
    }

    @Override
    public ProductCategory updateCategory(Long id, ProductCategory updatedCategory) {
        ProductCategory category = productCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        category.setCategoryName(updatedCategory.getCategoryName());
        return productCategoryRepository.save(category);
    }

    @Override
    public List<ProductCategory> getAll() {
        return productCategoryRepository.findAll();
    }

    @Override
    public Optional<ProductCategory> getById(Long id) {
        return productCategoryRepository.findById(id);
    }
}

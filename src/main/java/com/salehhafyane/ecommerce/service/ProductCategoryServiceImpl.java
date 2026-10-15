// Category CRUD for the admin page, converting between entity and DTO.
package com.salehhafyane.ecommerce.service;

import com.salehhafyane.ecommerce.dto.ProductCategoryDTO;
import com.salehhafyane.ecommerce.dto.ProductMapper;
import com.salehhafyane.ecommerce.entity.ProductCategory;
import com.salehhafyane.ecommerce.repository.ProductCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// DTO-facing implementation of IProductCategoryService.
@Service
public class ProductCategoryServiceImpl implements IProductCategoryService {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    // Creates a category from its name only; the id comes from the database.
    @Override
    public ProductCategoryDTO addCategory(ProductCategoryDTO categoryDTO) {
        ProductCategory category = new ProductCategory();
        category.setCategoryName(categoryDTO.getCategoryName());
        return ProductMapper.toDto(productCategoryRepository.save(category));
    }

    // Deletes by id; unknown ids simply do nothing at the repository level.
    @Override
    public void removeCategory(Long id) {
        productCategoryRepository.deleteById(id);
    }

    // Renames an existing category; throws 500 when the id doesn't exist.
    @Override
    public ProductCategoryDTO updateCategory(Long id, ProductCategoryDTO updatedCategory) {
        ProductCategory category = productCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        category.setCategoryName(updatedCategory.getCategoryName());
        return ProductMapper.toDto(productCategoryRepository.save(category));
    }

    // All categories as DTOs for the management table.
    @Override
    public List<ProductCategoryDTO> getAll() {
        return productCategoryRepository.findAll().stream()
                .map(ProductMapper::toDto)
                .toList();
    }

    // One category by id, or an empty Optional when unknown.
    @Override
    public Optional<ProductCategoryDTO> getById(Long id) {
        return productCategoryRepository.findById(id).map(ProductMapper::toDto);
    }
}

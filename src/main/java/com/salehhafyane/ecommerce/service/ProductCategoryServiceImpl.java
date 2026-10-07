package com.salehhafyane.ecommerce.service;

import com.salehhafyane.ecommerce.dto.ProductCategoryDTO;
import com.salehhafyane.ecommerce.dto.ProductMapper;
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
    public ProductCategoryDTO addCategory(ProductCategoryDTO categoryDTO) {
        ProductCategory category = new ProductCategory();
        category.setCategoryName(categoryDTO.getCategoryName());
        return ProductMapper.toDto(productCategoryRepository.save(category));
    }

    @Override
    public void removeCategory(Long id) {
        productCategoryRepository.deleteById(id);
    }

    @Override
    public ProductCategoryDTO updateCategory(Long id, ProductCategoryDTO updatedCategory) {
        ProductCategory category = productCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        category.setCategoryName(updatedCategory.getCategoryName());
        return ProductMapper.toDto(productCategoryRepository.save(category));
    }

    @Override
    public List<ProductCategoryDTO> getAll() {
        return productCategoryRepository.findAll().stream()
                .map(ProductMapper::toDto)
                .toList();
    }

    @Override
    public Optional<ProductCategoryDTO> getById(Long id) {
        return productCategoryRepository.findById(id).map(ProductMapper::toDto);
    }
}

// Product CRUD for the admin page, converting between entity and DTO.
package com.salehhafyane.ecommerce.service;

import com.salehhafyane.ecommerce.dto.ProductDTO;
import com.salehhafyane.ecommerce.dto.ProductMapper;
import com.salehhafyane.ecommerce.entity.Product;
import com.salehhafyane.ecommerce.entity.ProductCategory;
import com.salehhafyane.ecommerce.repository.ProductCategoryRepository;
import com.salehhafyane.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// DTO-facing implementation of IProductService.
@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    // Builds a fresh entity from the DTO and inserts it.
    @Override
    public void save(ProductDTO productDTO) {
        productRepository.save(toEntity(new Product(), productDTO));
    }

    // Deletes a product by id.
    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    // Loads the existing row then overwrites its fields; throws 500 when the id is unknown.
    @Override
    public void update(Long id, ProductDTO updatedProduct) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        productRepository.save(toEntity(product, updatedProduct));
    }

    // Whole catalog as DTOs.
    @Override
    public List<ProductDTO> getAll() {
        return productRepository.findAll().stream()
                .map(ProductMapper::toDto)
                .toList();
    }

    // One product by id, or an empty Optional when unknown.
    @Override
    public Optional<ProductDTO> getById(Long id) {
        return productRepository.findById(id).map(ProductMapper::toDto);
    }

    // Copies the DTO's fields onto the entity, resolving the category id to a real category row.
    private Product toEntity(Product product, ProductDTO dto) {
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setUnitPrice(dto.getUnitPrice());
        product.setImageUrl(dto.getImageUrl());
        product.setUnitsInStock(dto.getUnitsInStock());
        if (dto.getCategoryId() != null) {
            ProductCategory category = productCategoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found with id: " + dto.getCategoryId()));
            product.setCategory(category);
        }
        return product;
    }
}


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

@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Override
    public void save(ProductDTO productDTO) {
        productRepository.save(toEntity(new Product(), productDTO));
    }

    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public void update(Long id, ProductDTO updatedProduct) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        productRepository.save(toEntity(product, updatedProduct));
    }

    @Override
    public List<ProductDTO> getAll() {
        return productRepository.findAll().stream()
                .map(ProductMapper::toDto)
                .toList();
    }

    @Override
    public Optional<ProductDTO> getById(Long id) {
        return productRepository.findById(id).map(ProductMapper::toDto);
    }

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


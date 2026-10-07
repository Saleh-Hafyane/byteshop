package com.salehhafyane.ecommerce.service;

import com.salehhafyane.ecommerce.dto.ProductDTO;

import java.util.List;
import java.util.Optional;

public interface IProductService {
    void save(ProductDTO product);
    void delete(Long id);
    void update(Long id, ProductDTO updatedProduct);
    List<ProductDTO> getAll();
    Optional<ProductDTO> getById(Long id);
}


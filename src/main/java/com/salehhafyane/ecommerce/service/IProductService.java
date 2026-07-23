package com.salehhafyane.ecommerce.service;

import com.salehhafyane.ecommerce.entity.Product;

import java.util.List;
import java.util.Optional;

public interface IProductService {
    void save(Product product);
    void delete(Long id);
    void update(Long id, Product updatedProduct);
    List<Product> getAll();
    Optional<Product> getById(Long id);
}

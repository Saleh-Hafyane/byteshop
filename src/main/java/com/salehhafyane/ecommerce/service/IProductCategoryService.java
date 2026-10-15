// CRUD contract behind the admin's category management page.
package com.salehhafyane.ecommerce.service;

import com.salehhafyane.ecommerce.dto.ProductCategoryDTO;

import java.util.List;
import java.util.Optional;

public interface IProductCategoryService {
    ProductCategoryDTO addCategory(ProductCategoryDTO category); // create a new category
    void removeCategory(Long id); // delete one by id
    ProductCategoryDTO updateCategory(Long id, ProductCategoryDTO updatedCategory); // rename an existing category
    List<ProductCategoryDTO> getAll(); // full list for the management table
    Optional<ProductCategoryDTO> getById(Long id); // empty when the id is unknown
}

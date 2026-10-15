// CRUD contract behind the admin's product management page.
package com.salehhafyane.ecommerce.service;

import com.salehhafyane.ecommerce.dto.ProductDTO;

import java.util.List;
import java.util.Optional;

public interface IProductService {
    void save(ProductDTO product); // create a new catalog entry
    void delete(Long id); // remove one by id
    void update(Long id, ProductDTO updatedProduct); // overwrite the fields of an existing product
    List<ProductDTO> getAll(); // full catalog list
    Optional<ProductDTO> getById(Long id); // empty when the id is unknown
}


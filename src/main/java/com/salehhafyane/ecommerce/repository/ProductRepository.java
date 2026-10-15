// Product table access with the paged queries the catalog list runs.
package com.salehhafyane.ecommerce.repository;

import com.salehhafyane.ecommerce.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product,Long> {
    // Paged products of one category — called by /search/findByCategoryId.
    Page<Product> findByCategoryId(@Param("id") long id, Pageable pageable);
    // Paged keyword search on the product name — called by /search/findByNameContaining.
    Page<Product> findByNameContaining(@Param("name") String name,Pageable pageable);
}

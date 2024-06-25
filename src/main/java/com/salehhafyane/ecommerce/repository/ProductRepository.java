package com.salehhafyane.ecommerce.repository;

import com.salehhafyane.ecommerce.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
public interface ProductRepository extends JpaRepository<Product,Long> {
    Page<Product> findByCategoryId(@Param("id") long id, Pageable pageable);
    Page<Product> findByNameContaining(@Param("name") String name,Pageable pageable);
}

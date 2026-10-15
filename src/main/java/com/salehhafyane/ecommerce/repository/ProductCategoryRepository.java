// Category table access, exposed at /api/product-category for the menus.
package com.salehhafyane.ecommerce.repository;

import com.salehhafyane.ecommerce.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

// Spring Data REST serves the list at /api/product-category.
@RepositoryRestResource(collectionResourceRel = "productCategory", path = "product-category")
public interface ProductCategoryRepository extends JpaRepository<ProductCategory,Long> {
}

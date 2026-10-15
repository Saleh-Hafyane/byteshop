// Product group like "Laptops" or "Phones" used to organize the catalog.
package com.salehhafyane.ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

// Category row; deleting it cascades to its products.
@Entity
@Table(name = "product_category")
@Getter
@Setter
public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "category_name")
    private String categoryName; // label shown in the menu
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "category")
    private Set<Product> products; // products living in this category
}

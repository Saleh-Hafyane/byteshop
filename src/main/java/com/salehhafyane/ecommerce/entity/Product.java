// Catalog product for sale, with price, stock and an optional uploaded photo.
package com.salehhafyane.ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.util.Date;

// One row of the store catalog; stock is decremented when customers buy.
@Entity
@Table(name = "product")
@Data
public class  Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "category_id",nullable = false)
    private ProductCategory category; // shelf this product belongs to
    @Column(name = "name")
    private String name; // display title in listings
    @Column(name = "description")
    private String description; // marketing text on the detail page
    @Column(name = "unit_price")
    private BigDecimal unitPrice; // current selling price
    @Column(name = "image_url")
    private String imageUrl; // relative /api/files/... path or classic assets path
    @Column(name = "units_in_stock")
    private int unitsInStock; // available quantity, checked before adding to cart
    @Column(name = "date_created")
    @CreationTimestamp
    private Date dateCreated; // set automatically on first save
    @Column(name = "last_updated")
    @UpdateTimestamp
    private Date lastUpdated; // bumped automatically on every edit


}

// One product line inside an order, keeping the price it had when bought.
package com.salehhafyane.ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

// Immutable-ish line item: price and name are copied so later catalog changes don't alter past orders.
@Entity
@Table(name = "order_item")
@Getter
@Setter
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "image_url")
    private String imageUrl; // product picture at purchase time
    @Column(name = "unit_price")
    private BigDecimal unitPrice; // price per unit when the order was placed
    @Column(name = "quantity")
    private int quantity; // how many units the customer bought
    @Column(name = "product_id")
    private Long productId; // points back to the catalog product (not a JPA link, survives deletion)
    // Snapshot of the product name at purchase time; never updated afterwards.
    @Column(name = "product_name")
    private String productName;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order; // order this line belongs to

}

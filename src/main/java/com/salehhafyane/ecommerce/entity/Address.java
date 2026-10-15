// One shipping address attached to exactly one order.
package com.salehhafyane.ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

// Delivery location saved with the order; never serialized back as JSON by itself.
@Entity
@Table(name = "address")
@Getter
@Setter
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "city")
    private String city; // city name typed by the customer
    @Column(name = "full_address")
    private String fullAddress; // street line of the delivery address
    @JsonIgnore
    @OneToOne
    @PrimaryKeyJoinColumn
    private Order order; // owning order, shares the same primary key as Order.id
}

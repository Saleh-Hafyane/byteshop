// Delivery city chosen from the checkout dropdown.
package com.salehhafyane.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Data;

// Row in the cities list shown to customers during checkout.
@Entity
@Table(name = "city")
@Data
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "city_name")
    private String cityName; // display name of the city
}

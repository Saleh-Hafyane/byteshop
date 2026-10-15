// Purchase placed by a customer: totals, tracking, status, items and address.
package com.salehhafyane.ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

// The central order row; every money value on it is calculated by the server.
@Entity
@Table(name = "orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;



    @Column(name = "date_created")
    @CreationTimestamp
    private Date dateCreated; // when the customer placed the order
    @Column(name = "last_updated")
    @UpdateTimestamp
    private Date lastUpdated; // bumped automatically on every change

    @Column(name = "order_tracking_number", nullable = false)
    private String orderTrackingNumber; // server-generated UUID shown to the customer

    @Column(name = "status" )
    @Enumerated(EnumType.STRING)
    private OrderStatus status; // stored as text like "PENDING", never as a number

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice; // final amount the customer pays

    @Column(name = "total_quantity", nullable = false)
    private Integer totalQuantity; // sum of all item quantities

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // customer who placed the order

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
    private Set<OrderItem> orderItems = new HashSet<>(); // lines of the order, saved together with it
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address address; // where the order gets shipped

    // Links an item to this order on both sides so cascade save works.
    public void add(OrderItem item){
        if (item!=null){
            if (orderItems==null){
                orderItems = new HashSet<>();
            }
            orderItems.add(item);
            item.setOrder(this);
        }
    }


}

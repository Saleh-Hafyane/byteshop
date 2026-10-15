// Order table access; hidden from REST because orders go through dedicated controllers.
package com.salehhafyane.ecommerce.repository;

import com.salehhafyane.ecommerce.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

// exported=false so no anonymous /api/orders endpoint exists.
@RepositoryRestResource(exported = false)
public interface OrderRepository extends JpaRepository<Order, Long> {

    // One customer's orders, newest first — powers the "My Orders" page.
    List<Order> findByUserIdOrderByDateCreatedDesc(Long userId);

}
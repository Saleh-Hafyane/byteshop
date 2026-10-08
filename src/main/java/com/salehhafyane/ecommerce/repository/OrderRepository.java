package com.salehhafyane.ecommerce.repository;

import com.salehhafyane.ecommerce.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface OrderRepository extends JpaRepository<Order, Long> {

}
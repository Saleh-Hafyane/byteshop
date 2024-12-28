package com.salehhafyane.ecommerce.repository;

import com.salehhafyane.ecommerce.entity.Order;
import com.salehhafyane.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;

@CrossOrigin
public interface OrderRepository extends JpaRepository<Order, Long> {

}
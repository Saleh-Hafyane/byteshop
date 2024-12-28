package com.salehhafyane.ecommerce.repository;

import com.salehhafyane.ecommerce.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
public interface AddressRepository extends JpaRepository<Address,Long> {
}

package com.salehhafyane.ecommerce.repository;

import com.salehhafyane.ecommerce.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface AddressRepository extends JpaRepository<Address,Long> {
}

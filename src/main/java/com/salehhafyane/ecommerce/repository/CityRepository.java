package com.salehhafyane.ecommerce.repository;

import com.salehhafyane.ecommerce.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "city",path = "city")
public interface CityRepository extends JpaRepository<City,Long> {
}

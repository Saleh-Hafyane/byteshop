// City table access, exposed at /api/city for the checkout dropdown.
package com.salehhafyane.ecommerce.repository;

import com.salehhafyane.ecommerce.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

// Served by Spring Data REST under the short /api/city path the frontend calls.
@RepositoryRestResource(collectionResourceRel = "city",path = "city")
public interface CityRepository extends JpaRepository<City,Long> {
}

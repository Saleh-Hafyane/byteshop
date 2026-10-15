// Address table access; kept hidden from the auto-generated REST endpoints.
package com.salehhafyane.ecommerce.repository;

import com.salehhafyane.ecommerce.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

// exported=false blocks the /api/addresses endpoint — addresses are only reached through orders.
@RepositoryRestResource(exported = false)
public interface AddressRepository extends JpaRepository<Address,Long> {
}

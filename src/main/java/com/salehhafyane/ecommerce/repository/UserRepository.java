// User table access; hidden from REST — accounts are only touched by the auth flow.
package com.salehhafyane.ecommerce.repository;

import com.salehhafyane.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

// exported=false so passwords can never be read through a public endpoint.
@RepositoryRestResource(exported = false)
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username); // login lookup, empty when unknown
    boolean existsByUsername(String username); // checked before allowing a registration
    boolean existsByEmail(String email); // checked before allowing a registration
}

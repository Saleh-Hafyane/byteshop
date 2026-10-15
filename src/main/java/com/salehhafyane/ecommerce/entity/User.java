// Account entity that also acts as Spring Security's UserDetails for login.
package com.salehhafyane.ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigInteger;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
// Registered customer or admin; password here is only ever the bcrypt hash.
@Entity
@Table(name = "db_user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstname; // first name of the account holder
    private String lastname; // last name of the account holder
    @Column(unique = true)
    private String username; // unique login name
    @Column(unique = true)
    private String email; // unique contact address

    @JsonIgnore
    private String password; // bcrypt hash, never sent back in JSON responses
    @Enumerated(EnumType.STRING)
    private Role role; // USER or ADMIN, drives what the account may access
    @JsonIgnore
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private Set<Order> orders = new HashSet<>(); // everything this customer has bought

    // Links an order to this user on both sides so cascade save works.
    public void add(Order order){
        if (order!=null){
            if (orders==null){
                orders = new HashSet<>();
            }
            orders.add(order);
            order.setUser(this);
        }
    }


    // Spring Security reads roles as "ROLE_..." authorities, e.g. ROLE_ADMIN.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.toString()));
    }

    // The four checks below always pass: this app doesn't expire, lock or disable accounts.
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

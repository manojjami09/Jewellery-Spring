package com.Manoj.ecommerceBackend.repo;

import com.Manoj.ecommerceBackend.Entity.Cart;
import com.Manoj.ecommerceBackend.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}

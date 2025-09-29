package com.Manoj.ecommerceBackend.repo;

import com.Manoj.ecommerceBackend.Entity.Cart;
import com.Manoj.ecommerceBackend.Entity.CartItem;
import com.Manoj.ecommerceBackend.Entity.NewArrival;
import com.Manoj.ecommerceBackend.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
    Optional<CartItem> findByCartAndNewArrival(Cart cart, NewArrival newArrival);
}

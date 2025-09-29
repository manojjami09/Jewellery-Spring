package com.Manoj.ecommerceBackend.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="cartItems")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many cart items belong to one cart
    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    @JsonBackReference
    private Cart cart;

    // Product reference (optional, only set if it's a product)
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = true)
    private Product product;

    // NewArrival reference (optional, only set if it's a new arrival)
    @ManyToOne
    @JoinColumn(name = "new_arrival_id", nullable = true)
    private NewArrival newArrival;

    private int quantity;
}

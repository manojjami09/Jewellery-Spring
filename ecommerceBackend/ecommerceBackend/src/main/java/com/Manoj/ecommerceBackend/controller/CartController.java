package com.Manoj.ecommerceBackend.controller;

import com.Manoj.ecommerceBackend.Entity.Cart;
import com.Manoj.ecommerceBackend.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {


    private final CartService cartService;

    /**
     * Add product OR newArrival to user's cart
     */
    @PostMapping("/{userId}/add")
    public ResponseEntity<Cart> addToCart(
            @PathVariable Long userId,
            @RequestParam(required = false) Integer productId,
            @RequestParam(required = false) Integer newArrivalId,
            @RequestParam int quantity
    ) {
        // Validate that at least one item is provided
        if (productId == null && newArrivalId == null) {
            return ResponseEntity.badRequest().build();
        }

        // Call service to add item
        return ResponseEntity.ok(
                cartService.addToCart(userId, productId, newArrivalId, quantity)
        );
    }


    /**
     * Get user's cart with all items
     */
    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCart(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    /**
     * Remove item from cart by cartItemId
     */
    @DeleteMapping("/{userId}/remove/{cartItemId}")
    public ResponseEntity<Void> removeCartItem(@PathVariable Long cartItemId) {
        cartService.removeCartItem(cartItemId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Clear cart when placing order
     */
    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<Cart> clearCart(@PathVariable Long userId) {
        Cart clearedCart = cartService.clearCart(userId);
        return ResponseEntity.ok(clearedCart);
    }

    /**
     * Update cart item quantity
     */
    @PutMapping("/{userId}/update/{cartItemId}")
    public ResponseEntity<Cart> updateQuantity(
            @PathVariable Long userId,
            @PathVariable Long cartItemId,
            @RequestParam int quantity
    ) {
        Cart updatedCart = cartService.updateQuantity(userId, cartItemId, quantity);
        return ResponseEntity.ok(updatedCart);
    }
}

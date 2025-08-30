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

    private final CartService cartService;  // use constructor injection instead of @Autowired

    /**
     * Add product to user's cart.
     */
    @PostMapping("/{userId}/add")
    public ResponseEntity<Cart> addToCart(
            @PathVariable Long userId,
            @RequestParam Integer productId,   // ✅ Changed to Long (matches DB & service)
            @RequestParam int quantity) {   // ✅ int is fine for quantity
        return ResponseEntity.ok(cartService.addToCart(userId, productId, quantity));
    }

    /**
     * Get user's cart with all items.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCart(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    /**
     * Remove item from cart by cartItemId.
     */
    @DeleteMapping("/{userId}/remove/{cartItemId}")
    public ResponseEntity<Void> removeCartItem(@PathVariable Long cartItemId) {
        cartService.removeCartItem(cartItemId);
        return ResponseEntity.noContent().build();
    }

    //clearing cart when placing order
    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<Cart> clearCart(@PathVariable Long userId) {
        Cart clearedCart = cartService.clearCart(userId);
        return ResponseEntity.ok(clearedCart);
    }
    //updating cart quantity
    @PutMapping("/{userId}/update/{cartItemId}")
    public ResponseEntity<Cart> updateQuantity(
            @PathVariable Long userId,
            @PathVariable Long cartItemId,
            @RequestParam int quantity) {
        Cart updatedCart = cartService.updateQuantity(userId, cartItemId, quantity);
        return ResponseEntity.ok(updatedCart);
    }

}

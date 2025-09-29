package com.Manoj.ecommerceBackend.service;

import com.Manoj.ecommerceBackend.Entity.*;
import com.Manoj.ecommerceBackend.repo.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final NewArrivalsRepository newArrivalsRepository;

    /**
     * Add a product to user's cart (create cart if not exists).
     */
    @Transactional
    public Cart addToCart(Long userId, Integer productId, Integer newArrivalId, int quantity) {
        // fetch user
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // fetch or create cart
        var cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = Cart.builder().user(user).build();
            return cartRepository.save(newCart);
        });

        // Case 1: Product
        if (productId != null) {
            var product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            var existingItem = cartItemRepository.findByCartAndProduct(cart, product);
            if (existingItem.isPresent()) {
                existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
                cartItemRepository.save(existingItem.get());
            } else {
                CartItem newItem = CartItem.builder()
                        .cart(cart)
                        .product(product)
                        .quantity(quantity)
                        .build();
                cartItemRepository.save(newItem);
                cart.getItems().add(newItem);
            }
        }

        // Case 2: NewArrival
        else if (newArrivalId != null) {
            var newArrival = newArrivalsRepository.findById(newArrivalId)
                    .orElseThrow(() -> new RuntimeException("New Arrival not found"));

            var existingItem = cartItemRepository.findByCartAndNewArrival(cart, newArrival);
            if (existingItem.isPresent()) {
                existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
                cartItemRepository.save(existingItem.get());
            } else {
                CartItem newItem = CartItem.builder()
                        .cart(cart)
                        .newArrival(newArrival)
                        .quantity(quantity)
                        .build();
                cartItemRepository.save(newItem);
                cart.getItems().add(newItem);
            }
        }

        else {
            throw new RuntimeException("Either productId or newArrivalId must be provided");
        }

        return cartRepository.save(cart);
    }


    /**
     * Get user's cart with items.
     */
    public Cart getCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
    }

    /**
     * Remove a cart item by ID.
     */
    @Transactional
    public void removeCartItem(Long cartItemId) {
        if (!cartItemRepository.existsById(cartItemId)) {
            throw new RuntimeException("Cart item not found");
        }
        cartItemRepository.deleteById(cartItemId);
    }
    //clear cart
    @Transactional
    public Cart clearCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.getItems().clear(); // orphanRemoval will delete CartItems
        return cartRepository.save(cart);
    }

    //update cart
    @Transactional
    public Cart updateQuantity(Long userId, Long cartItemId, int quantity) {
        // 1. Find user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Get user's cart
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        // 3. Find the cart item
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        // 4. Ensure item belongs to this cart
        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("Cart item does not belong to this user's cart");
        }

        // 5. Update quantity
        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);

        return cart;  // return updated cart
    }
}

package com.Manoj.ecommerceBackend.service;

import com.Manoj.ecommerceBackend.Entity.Cart;
import com.Manoj.ecommerceBackend.Entity.CartItem;
import com.Manoj.ecommerceBackend.Entity.Product;
import com.Manoj.ecommerceBackend.Entity.User;
import com.Manoj.ecommerceBackend.repo.CartItemRepository;
import com.Manoj.ecommerceBackend.repo.CartRepository;
import com.Manoj.ecommerceBackend.repo.ProductRepository;
import com.Manoj.ecommerceBackend.repo.UserRepository;
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

    /**
     * Add a product to user's cart (create cart if not exists).
     */
    @Transactional
    public Cart addToCart(Long userId, Integer productId, int quantity) {
        // 1. Find the user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Get or create the cart
        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = Cart.builder()
                    .user(user)
                    .build();
            return cartRepository.save(newCart);
        });

        // 3. Find product
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // 4. Check if item already in cart
        Optional<CartItem> existingItem = cartItemRepository.findByCartAndProduct(cart, product);

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepository.save(item);
        } else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(quantity)
                    .build();
            cartItemRepository.save(newItem);
            cart.getItems().add(newItem); // maintain both sides
        }

        return cartRepository.save(cart); // ensure cart is updated
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

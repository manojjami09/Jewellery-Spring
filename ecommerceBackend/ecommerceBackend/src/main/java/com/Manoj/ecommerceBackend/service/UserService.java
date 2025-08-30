package com.Manoj.ecommerceBackend.service;

import com.Manoj.ecommerceBackend.Entity.Cart;
import com.Manoj.ecommerceBackend.Entity.User;
import com.Manoj.ecommerceBackend.dto.LoginRequest;
import com.Manoj.ecommerceBackend.dto.LoginResponse;
import com.Manoj.ecommerceBackend.dto.SignupRequest;
import com.Manoj.ecommerceBackend.repo.CartRepository;
import com.Manoj.ecommerceBackend.repo.UserRepository;
import com.Manoj.ecommerceBackend.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final CartRepository cartRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.cartRepository = cartRepository;
        this.jwtUtil = jwtUtil;
    }

    public User signup(@RequestBody SignupRequest request){

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already taken");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(user);

        // Create empty cart for new user
        Cart cart = new Cart();
        cart.setUser(savedUser);
        cartRepository.save(cart); // cartId will auto-generate

        return savedUser;

    }
    public LoginResponse login(LoginRequest request) {
        User dbUser = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), dbUser.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(dbUser);

        return new LoginResponse(token, dbUser.getId(), "Login successful");
    }


}

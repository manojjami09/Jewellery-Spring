package com.Manoj.ecommerceBackend.controller;

import com.Manoj.ecommerceBackend.Entity.User;
import com.Manoj.ecommerceBackend.dto.LoginRequest;
import com.Manoj.ecommerceBackend.dto.LoginResponse;
import com.Manoj.ecommerceBackend.dto.SignupRequest;
import com.Manoj.ecommerceBackend.dto.SignupResponse;
import com.Manoj.ecommerceBackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signpUser(@RequestBody SignupRequest request){
        User savedUser=userService.signup(request);
        return new ResponseEntity<>(new SignupResponse(savedUser.getId(),"User successfully created"), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }

}

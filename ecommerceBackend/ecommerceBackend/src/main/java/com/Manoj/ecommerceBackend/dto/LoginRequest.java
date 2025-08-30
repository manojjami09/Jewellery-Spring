package com.Manoj.ecommerceBackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequest {
    private String username;  // or email, depending on your choice
    private String password;
}

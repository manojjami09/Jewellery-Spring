package com.Manoj.ecommerceBackend.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private Long userId;
    private String message;

    public LoginResponse(String token, Long userId, String message) {
        this.token = token;
        this.userId = userId;
        this.message = message;
    }
}

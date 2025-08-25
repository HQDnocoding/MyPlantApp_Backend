package com.dat.plantbackend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private boolean success;
    private String accessToken;
    private String refreshToken;
    private UserDTO user;
    private String message;
    private long expiresIn;
    private long refreshTokenExpiresIn;
}
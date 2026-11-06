package com.example.find_your_partner.auth.api;

public record LoginResponse(
        String accessToken,
        String tokenType,
        long expiresIn
) {
}

package com.example.find_your_partner.auth.api;

import com.example.find_your_partner.user.domain.UserStatus;

import java.time.Instant;
import java.util.UUID;

public record RegisterResponse(
        UUID userId,
        String email,
        String phoneNumber,
        UserStatus status,
        Instant createdAt
) {
}

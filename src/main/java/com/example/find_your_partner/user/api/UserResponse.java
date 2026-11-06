package com.example.find_your_partner.user.api;

import com.example.find_your_partner.user.domain.UserStatus;

import java.time.Instant;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String email,
        String phoneNumber,
        UserStatus status,
        Instant createdAt
){
}

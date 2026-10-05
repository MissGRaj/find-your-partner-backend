package com.example.find_your_partner.common.exception;

import java.time.Instant;

public record ApiError(String code, String message, Instant timestamp) {
    
}

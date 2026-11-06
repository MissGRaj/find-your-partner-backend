package com.example.find_your_partner.common.exception;

import java.time.Instant;
import java.util.List;

public record ApiError(String code, String message, Instant timestamp, List<FieldError> errors) {
    
}

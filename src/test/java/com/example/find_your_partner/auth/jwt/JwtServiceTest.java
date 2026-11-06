package com.example.find_your_partner.auth.jwt;


import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setup() {

        String secret = Base64.getEncoder()
                .encodeToString(
                        "12345678901234567890123456789012".getBytes()
                );

        JwtProperties properties = new JwtProperties(
                secret,
                Duration.ofMinutes(15)
        );

        jwtService = new JwtService(properties);
    }

    @Test
    void shouldGenerateAndValidateAccessToken() {
        UUID userId = UUID.randomUUID();
        String email = "Miya@example.com";

        String token = jwtService.generateAccessToken(userId, email);

        assertNotNull(token);

        Claims claims = jwtService.parseAndValidate(token);

        assertEquals(userId.toString(), claims.getSubject());
        assertEquals(email, claims.get("email"));
        assertEquals("access", claims.get("token_type"));
    }

    @Test
    void shouldExtractUserId(){

        UUID userId = UUID.randomUUID();

        String token = jwtService.generateAccessToken(userId, "maya@example.com");

        UUID extractedUserId = jwtService.extractUserId(token);
        assertEquals(userId, extractedUserId);
    }

    @Test
    void tokenShouldHaveFutureExpiration() {

        String token = jwtService.generateAccessToken(
                UUID.randomUUID(),
                "athi@example.com"
        );

        Instant expiration = jwtService.extractExpiration(token);
        assertTrue(expiration.isAfter(Instant.now()));
    }
}

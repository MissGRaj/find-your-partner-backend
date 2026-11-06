package com.example.find_your_partner.auth.jwt;

import com.example.find_your_partner.user.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

@Service
public class JwtService {

    private final JwtProperties jwtProperties;
    private final SecretKey signingKey;

    public JwtService(JwtProperties jwtProperties){
        this.jwtProperties = jwtProperties;

        byte[] keyBytes = Base64.getDecoder().decode(jwtProperties.secret());

        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public  String generateAccessToken(UUID userId, String email){
        Instant now = Instant.now();
        Instant expiresAt = now.plus(jwtProperties.accessTokenExpiration());

        return Jwts.builder()
                .subject(userId.toString())
                .claim("email", email)
                .claim("token_type", "access")
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiresAt))
                .signWith(signingKey)
                .compact();
    }

    public Claims parseAndValidate(String token){
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public UUID extractUserId(String token){
        String subject = parseAndValidate(token).getSubject();
        return UUID.fromString(subject);
    }

    public Instant extractExpiration(String token){
        Date expiration = parseAndValidate(token).getExpiration();
        return expiration.toInstant();
    }
}

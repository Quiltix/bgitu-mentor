package com.bgitu.mentor.auth.security;

import com.bgitu.mentor.auth.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final SecretKey jwtSecret ;
    private final long jwtExpirationInMs = 86400000;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret) {
        this.jwtSecret = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

    }

    public String generateToken(Long userId, Role role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(String.valueOf(userId)) // Устанавливаем ID как subject токена
                .claim("role", role.name())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(jwtSecret)
                .compact();
    }

    public Long getIdFromToken(String token) {
        Claims claims = parseClaims(token);
        return Long.parseLong(claims.getSubject());
    }



    public Role getRoleFromToken(String token) {
        try {

            String roleString = parseClaims(token).get("role", String.class);
            if (roleString == null) {
                throw new IllegalArgumentException("В токене отсутствует поле 'role'.");
            }

            return Role.valueOf(roleString);
        } catch (IllegalArgumentException e) {

            throw new JwtException("Некорректная роль в токене", e);
        }
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtSecret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

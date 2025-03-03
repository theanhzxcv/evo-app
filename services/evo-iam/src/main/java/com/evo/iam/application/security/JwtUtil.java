package com.evo.iam.application.security;

import com.evo.iam.domain.User;
import com.evo.iam.infrastructure.persistence.entities.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtDecoder jwtDecoder;
    private final RSAKeysUtil rsaKeysUtil;
    private final static long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 30;
    private final static long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 5;

    public String generateToken(User user, long expirationTime) throws Exception {
        PrivateKey privateKey = rsaKeysUtil.getPrivateKey();
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .setId(UUID.randomUUID().toString())
                .claim("user_id", user.getId())
//                .claim("role", buildScope(userDetails))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    public String generateAccessToken(User user) throws Exception {
        return generateToken(user, ACCESS_TOKEN_EXPIRATION);
    }

    public String generateRefreshToken(User user) throws Exception {
        return generateToken(user, REFRESH_TOKEN_EXPIRATION);
    }

    public Claims extractClaims(String token) {
        try {
            PublicKey publicKey = rsaKeysUtil.getPublicKey();

            return Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    public Date getSystemJwtExpirationTime(String token) {
        try {
            return extractClaims(token).getExpiration();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}

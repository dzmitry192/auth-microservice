package com.innowise.authmicroservice.utils;

import com.innowise.authmicroservice.entity.ClientEntity;
import com.innowise.authmicroservice.entity.RefreshTokenEntity;
import com.innowise.authmicroservice.repository.ClientRepository;
import io.jsonwebtoken.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    @Value("${jwt.secret.access}")
    private String jwtAccessSecret;
    @Value("${jwt.secret.refresh}")
    private String jwtRefreshSecret;
    @NonNull
    private ClientRepository clientRepository;

    public String generateAccessToken(@NonNull ClientEntity client) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationInstant = now.plusMinutes(5).atZone(ZoneId.systemDefault()).toInstant();
        final Date accessExpiration = Date.from(accessExpirationInstant);
        return Jwts.builder()
                .setSubject(client.getEmail())
                .setExpiration(accessExpiration)
                .signWith(SignatureAlgorithm.HS384, jwtAccessSecret)
                .claim("roles", client.getRole())
                .compact();
    }

    public String getSubject(String token) {
        return Jwts.claims().getSubject();
    }

    public ClientEntity getClientFromToken(String token) {
        return clientRepository.findByEmail(getSubject(token));
    }

    public RefreshTokenEntity generateRefreshToken(@NonNull ClientEntity client) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant refreshExpirationInstant = now.plusDays(30).atZone(ZoneId.systemDefault()).toInstant();
        final Date refreshExpiration = Date.from(refreshExpirationInstant);

        String refreshToken = Jwts.builder()
                .setSubject(client.getEmail())
                .setExpiration(refreshExpiration)
                .signWith(SignatureAlgorithm.HS384, jwtRefreshSecret)
                .compact();

        return new RefreshTokenEntity(refreshToken, refreshExpiration, client.getId());
    }

    public boolean validateAccessToken(@NonNull String accessToken) {
        return validateToken(accessToken, jwtAccessSecret);
    }

    public boolean validateRefreshToken(@NonNull String refreshToken) {
        return validateToken(refreshToken, jwtRefreshSecret);
    }

    private boolean validateToken(String token, @NonNull String secret) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secret.getBytes())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            logger.error("Token expired", expEx);
        } catch (UnsupportedJwtException unsEx) {
            logger.error("Unsupported jwt", unsEx);
        } catch (MalformedJwtException mjEx) {
            logger.error("Malformed jwt", mjEx);
        } catch (Exception e) {
            logger.error("invalid token", e);
        }
        return false;
    }

    public Claims getAccessClaims(@NonNull String token) {
        return getClaims(token, jwtAccessSecret);
    }

    public Claims getRefreshClaims(@NonNull String token) {
        return getClaims(token, jwtRefreshSecret);
    }

    private Claims getClaims(String token, @NonNull String secret) {
        return Jwts.parserBuilder()
                .setSigningKey(secret.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
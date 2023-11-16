package com.innowise.authmicroservice.utils;

import com.innowise.authmicroservice.entity.ClientEntity;
import com.innowise.authmicroservice.entity.RefreshTokenEntity;
import com.innowise.authmicroservice.repository.ClientRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.avro.util.Utf8;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
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
                .subject(client.getEmail())
                .claim("roles", client.getRole().name())
                .expiration(accessExpiration)
                .signWith(SignatureAlgorithm.HS256, jwtAccessSecret.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    public String getSubject(String accessToken) {
        return Jwts.parser()
                .setSigningKey(jwtAccessSecret.getBytes(StandardCharsets.UTF_8))
                .build()
                .parseClaimsJws(accessToken)
                .getPayload()
                .getSubject();
    }

    public ClientEntity getClientFromToken(String token) {
        return clientRepository.findByEmail(getSubject(token));
    }

    public RefreshTokenEntity generateRefreshToken(@NonNull ClientEntity client) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant refreshExpirationInstant = now.plusDays(30).atZone(ZoneId.systemDefault()).toInstant();
        final Date refreshExpiration = Date.from(refreshExpirationInstant);

        String refreshToken = Jwts.builder()
                .subject(client.getEmail())
                .expiration(refreshExpiration)
                .signWith(SignatureAlgorithm.HS256, jwtRefreshSecret.getBytes(StandardCharsets.UTF_8))
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
            Jwts.parser()
                    .setSigningKey(secret.getBytes(StandardCharsets.UTF_8))
                    .build()
                    .parseClaimsJws(token);

            return true;
        } catch (ExpiredJwtException expEx) {
            logger.error("Token expired", expEx);
        } catch (UnsupportedJwtException unsEx) {
            logger.error("Unsupported jwt", unsEx);
        } catch (MalformedJwtException mjEx) {
            logger.error("Malformed jwt", mjEx);
        } catch (SignatureException e) {
            logger.error("SignatureException", e);
        } catch (IllegalArgumentException e) {
            logger.error("IllegalArgumentException", e);
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
        return Jwts.parser()
                .setSigningKey(secret.getBytes())
                .build()
                .parseClaimsJws(token)
                .getPayload();
    }
}
package com.innowise.authmicroservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@Entity
@Table(name = "refreshtokens")
@NoArgsConstructor
@RequiredArgsConstructor
public class RefreshTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private String token;
    @NonNull
    private Date expiresAt;
    @NonNull
    private Long clientId;
}

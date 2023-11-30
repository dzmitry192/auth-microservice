package com.innowise.authmicroservice.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshTokenRequest {
    @NotBlank(message = "Refresh token can't be empty")
    private String refreshToken;
}

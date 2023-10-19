package com.innowise.authmicroservice.payload.response;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SignupAndRefreshTokenResponse {
    @NonNull
    private String accessToken;
    @NonNull
    private String refreshToken;
    private final String TYPE = "Bearer ";
}
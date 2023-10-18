package com.innowise.authmicroservice.payload.response;

import lombok.Data;

@Data
public class SignupAndRefreshTokenResponse {
    private String accessToken;
    private String refreshToken;
    private String type = "Bearer";

    public SignupAndRefreshTokenResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
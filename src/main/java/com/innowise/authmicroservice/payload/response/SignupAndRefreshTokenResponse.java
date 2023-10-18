package com.innowise.authmicroservice.payload.response;

import lombok.Data;

@Data
public class SignupAndRefreshTokenResponse {
    private String accessToken;
    private String refreshToken;
    private String type;
}
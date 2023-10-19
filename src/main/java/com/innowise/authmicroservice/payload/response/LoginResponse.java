package com.innowise.authmicroservice.payload.response;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class LoginResponse {
    @NonNull
    private String accessToken;
    private final String TYPE = "Bearer ";
}

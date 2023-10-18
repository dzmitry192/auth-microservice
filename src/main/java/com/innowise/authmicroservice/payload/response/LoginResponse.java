package com.innowise.authmicroservice.payload.response;

import lombok.Data;

@Data
public class LoginResponse {
    private String accessToken;
    private String type = "Bearer";

    public LoginResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}

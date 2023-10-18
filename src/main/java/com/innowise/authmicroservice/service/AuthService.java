package com.innowise.authmicroservice.service;

import com.innowise.authmicroservice.payload.request.LoginRequest;
import com.innowise.authmicroservice.payload.request.RefreshTokenRequest;
import com.innowise.authmicroservice.payload.request.SignupRequest;
import com.innowise.authmicroservice.payload.response.LoginResponse;
import com.innowise.authmicroservice.payload.response.SignupAndRefreshTokenResponse;

public interface AuthService {
    LoginResponse login(LoginRequest login);
    SignupAndRefreshTokenResponse signup(SignupRequest signup) throws Exception;
//    SignupAndRefreshTokenResponse refresh(RefreshTokenRequest refreshToken);
}

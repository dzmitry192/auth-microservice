package com.innowise.authmicroservice.service;

import com.innowise.authmicroservice.exception.ClientAlreadyExistsException;
import com.innowise.authmicroservice.exception.InvalidTokenException;
import com.innowise.authmicroservice.exception.NotFoundException;
import com.innowise.authmicroservice.payload.request.LoginRequest;
import com.innowise.authmicroservice.payload.request.RefreshTokenRequest;
import com.innowise.authmicroservice.payload.request.SignupRequest;
import com.innowise.authmicroservice.payload.response.LoginResponse;
import com.innowise.authmicroservice.payload.response.SignupAndRefreshTokenResponse;

import java.text.ParseException;

public interface AuthService {
    LoginResponse login(LoginRequest login) throws NotFoundException, ParseException;

    SignupAndRefreshTokenResponse signup(SignupRequest signup) throws ClientAlreadyExistsException, ParseException;

    SignupAndRefreshTokenResponse refresh(RefreshTokenRequest refreshToken) throws NotFoundException, InvalidTokenException, ParseException;
}

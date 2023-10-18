package com.innowise.authmicroservice.controller;

import com.innowise.authmicroservice.payload.request.LoginRequest;
import com.innowise.authmicroservice.payload.request.RefreshTokenRequest;
import com.innowise.authmicroservice.payload.request.SignupRequest;
import com.innowise.authmicroservice.payload.response.LoginResponse;
import com.innowise.authmicroservice.payload.response.SignupAndRefreshTokenResponse;
import com.innowise.authmicroservice.service.impl.AuthServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthServiceImpl authService;

    public AuthController(AuthServiceImpl authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok().body(authService.login(loginRequest));
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupAndRefreshTokenResponse> signup(@Valid @RequestBody SignupRequest signupRequest) throws Exception {
        return ResponseEntity.ok().body(authService.signup(signupRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<SignupAndRefreshTokenResponse> refresh(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) throws Exception {
        return ResponseEntity.ok().body(authService.refresh(refreshTokenRequest));
    }
}

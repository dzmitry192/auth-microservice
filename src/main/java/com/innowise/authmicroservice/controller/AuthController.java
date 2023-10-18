package com.innowise.authmicroservice.controller;

import com.innowise.authmicroservice.payload.request.LoginRequest;
import com.innowise.authmicroservice.payload.response.LoginResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
}

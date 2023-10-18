package com.innowise.authmicroservice.service.impl;

import com.innowise.authmicroservice.entity.ClientEntity;
import com.innowise.authmicroservice.entity.Role;
import com.innowise.authmicroservice.payload.request.LoginRequest;
import com.innowise.authmicroservice.payload.request.SignupRequest;
import com.innowise.authmicroservice.payload.response.LoginResponse;
import com.innowise.authmicroservice.payload.response.SignupAndRefreshTokenResponse;
import com.innowise.authmicroservice.repository.ClientRepository;
import com.innowise.authmicroservice.service.AuthService;
import com.innowise.authmicroservice.utils.JwtUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final JwtUtils jwtUtils;
    private final ClientRepository clientRepository;

    public AuthServiceImpl(JwtUtils jwtUtils, ClientRepository clientRepository) {
        this.jwtUtils = jwtUtils;
        this.clientRepository = clientRepository;
    }

    @Override
    public LoginResponse login(LoginRequest login) {
        ClientEntity client = clientRepository.findByEmail(login.getEmail());
        if(client == null) {
            throw new UsernameNotFoundException("User Not Found!");
        }

        return new LoginResponse();
    }

    @Override
    public SignupAndRefreshTokenResponse signup(SignupRequest signup) throws Exception {
        if(clientRepository.existsByEmail(signup.getEmail())) {
            throw new Exception("User is already exists!");
        }

        ClientEntity client = new ClientEntity(signup.getFirstName(),
                signup.getLastName(),
                signup.getEmail(),
                signup.getPassword(),
                signup.getPhoneNumber(),
                signup.getAddress(),
                Role.ROLE_USER);
        clientRepository.save(client);

        String accessToken = jwtUtils.generateAccessToken(client);
        String refreshToken = jwtUtils.generateRefreshToken(client);

        return new SignupAndRefreshTokenResponse(accessToken, refreshToken);
    }
}

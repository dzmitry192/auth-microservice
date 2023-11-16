package com.innowise.authmicroservice.service.impl;

import com.innowise.authmicroservice.entity.ClientEntity;
import com.innowise.authmicroservice.entity.RefreshTokenEntity;
import com.innowise.authmicroservice.entity.Role;
import com.innowise.authmicroservice.payload.request.LoginRequest;
import com.innowise.authmicroservice.payload.request.RefreshTokenRequest;
import com.innowise.authmicroservice.payload.request.SignupRequest;
import com.innowise.authmicroservice.payload.response.LoginResponse;
import com.innowise.authmicroservice.payload.response.SignupAndRefreshTokenResponse;
import com.innowise.authmicroservice.repository.ClientRepository;
import com.innowise.authmicroservice.repository.RefreshTokenRepository;
import com.innowise.authmicroservice.service.AuthService;
import com.innowise.authmicroservice.utils.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtUtils jwtUtils;
    private final ClientRepository clientRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public LoginResponse login(LoginRequest login) {
        ClientEntity client = clientRepository.findByEmail(login.getEmail());
        if (client == null) {
            throw new UsernameNotFoundException("User Not Found!");
        }

        return new LoginResponse(jwtUtils.generateAccessToken(client));
    }

    @Override
    public SignupAndRefreshTokenResponse signup(SignupRequest signup) throws Exception {
        if (clientRepository.existsByEmail(signup.getEmail())) {
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
        RefreshTokenEntity refreshToken = jwtUtils.generateRefreshToken(client);

        refreshTokenRepository.save(refreshToken);

        return new SignupAndRefreshTokenResponse(accessToken, refreshToken.getToken());
    }

    @Override
    public SignupAndRefreshTokenResponse refresh(RefreshTokenRequest refreshTokenRequest) throws Exception {
        if (jwtUtils.validateRefreshToken(refreshTokenRequest.getRefreshToken())) {
            RefreshTokenEntity refreshToken = refreshTokenRepository.findByToken(refreshTokenRequest.getRefreshToken());
            Optional<ClientEntity> client = clientRepository.findById(refreshToken.getClientId());
            if (client.isPresent()) {
                String accessToken = jwtUtils.generateAccessToken(client.get());
                return new SignupAndRefreshTokenResponse(accessToken, refreshToken.getToken());
            } else {
                throw new UsernameNotFoundException("User Not Found");
            }
        } else {
            throw new Exception("Token is not valid");
        }
    }
}

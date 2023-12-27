package com.innowise.authmicroservice.service.impl;

import avro.ClientActionRequest;
import avro.NotificationRequest;
import com.innowise.authmicroservice.entity.ClientEntity;
import com.innowise.authmicroservice.entity.RefreshTokenEntity;
import com.innowise.authmicroservice.entity.Role;
import com.innowise.authmicroservice.enums.ActionEnum;
import com.innowise.authmicroservice.enums.ActionTypeEnum;
import com.innowise.authmicroservice.exception.ClientAlreadyExistsException;
import com.innowise.authmicroservice.exception.InvalidTokenException;
import com.innowise.authmicroservice.exception.NotFoundException;
import com.innowise.authmicroservice.kafka.KafkaProducer;
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
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtUtils jwtUtils;
    private final ClientRepository clientRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final KafkaProducer kafkaProducer;

    @Override
    public LoginResponse login(LoginRequest login) throws NotFoundException, ParseException {
        ClientEntity client = clientRepository.findByEmail(login.getEmail());
        if (client == null) {
            throw new NotFoundException("Client with email = " + login.getEmail() + " not found");
        }

        kafkaProducer.sendClientActionRequest(new ClientActionRequest(login.getEmail(), ActionEnum.LOGIN_ACTION.getAction(), ActionTypeEnum.LOGIN_ACTION_TYPE.getActionType(), LocalDateTime.now().toString()));

        return new LoginResponse(jwtUtils.generateAccessToken(client));
    }

    @Override
    public SignupAndRefreshTokenResponse signup(SignupRequest signup) throws ClientAlreadyExistsException, ParseException {
        if (clientRepository.existsByEmail(signup.getEmail())) {
            throw new ClientAlreadyExistsException("Client with email = " + signup.getEmail() + " already exists");
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

        kafkaProducer.sendNotificationRequest(new NotificationRequest(
                client.getEmail(),
                "Регистрация",
                "Вы успешно зарегистрировались на сайте"
        ));

        kafkaProducer.sendClientActionRequest(new ClientActionRequest(signup.getEmail(), ActionEnum.SIGNUP_ACTION.getAction(), ActionTypeEnum.SIGNUP_ACTION_TYPE.getActionType(), LocalDateTime.now().toString()));

        return new SignupAndRefreshTokenResponse(accessToken, refreshToken.getToken());
    }

    @Override
    public SignupAndRefreshTokenResponse refresh(RefreshTokenRequest refreshTokenRequest) throws NotFoundException, InvalidTokenException, ParseException {
        if (jwtUtils.validateRefreshToken(refreshTokenRequest.getRefreshToken())) {
            RefreshTokenEntity refreshToken = refreshTokenRepository.findByToken(refreshTokenRequest.getRefreshToken());
            Optional<ClientEntity> client = clientRepository.findById(refreshToken.getClientId());
            if (client.isPresent()) {
                String accessToken = jwtUtils.generateAccessToken(client.get());

                kafkaProducer.sendClientActionRequest(new ClientActionRequest(client.get().getEmail(), ActionEnum.REFRESH_ACTION.getAction(), ActionTypeEnum.REFRESH_ACTION_TYPE.getActionType(), LocalDateTime.now().toString()));

                return new SignupAndRefreshTokenResponse(accessToken, refreshToken.getToken());
            } else {
                throw new NotFoundException("Client not found");
            }
        } else {
            throw new InvalidTokenException("Token isn't valid");
        }
    }
}

package com.innowise.authmicroservice;

import com.innowise.authmicroservice.entity.ClientEntity;
import com.innowise.authmicroservice.entity.RefreshTokenEntity;
import com.innowise.authmicroservice.entity.Role;
import com.innowise.authmicroservice.repository.ClientRepository;
import com.innowise.authmicroservice.repository.RefreshTokenRepository;
import com.innowise.authmicroservice.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@RequiredArgsConstructor
public class AuthMicroserviceApplication {

    private final ClientRepository clientRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtils jwtUtils;
    public static void main(String[] args) {
        SpringApplication.run(AuthMicroserviceApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            ClientEntity admin = new ClientEntity("ADMIN", "ADMIN", "admin@gmail.com", "admin", "+375333065607", "dom admina", Role.ROLE_ADMIN);
            String accessToken = jwtUtils.generateAccessToken(admin);
            RefreshTokenEntity refreshToken = jwtUtils.generateRefreshToken(clientRepository.save(admin));

            refreshTokenRepository.save(refreshToken);

            System.out.println("ACCESS: " + accessToken);
            System.out.println("REFRESH: " + refreshToken.getToken());
        };
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

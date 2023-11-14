package com.innowise.authmicroservice.kafka;

import com.innowise.authmicroservice.avro.TokenRequest;
import com.innowise.authmicroservice.avro.TokenResponse;
import com.innowise.authmicroservice.avro.UserDetailsDto;
import com.innowise.authmicroservice.entity.ClientEntity;
import com.innowise.authmicroservice.utils.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class KafkaListeners {

    private JwtUtils jwtUtils;
    private KafkaProducer kafkaProducer;

    @KafkaListener(topics = "${kafka.topics.token_request}", groupId = "token_request_id")
    public void tokenRequestListener(TokenRequest tokenRequest) {
        System.out.println("Request for validateToken was received!");
        kafkaProducer.sendTokenResponse(new TokenResponse(jwtUtils.validateAccessToken((String) tokenRequest.getToken())));
    }

    @KafkaListener(topics = "${kafka.topics.user_details}", groupId = "user_details_id")
    public void userDetailsListener(TokenRequest tokenRequest) {
        System.out.println("Request for user_details was received!");
        ClientEntity client = jwtUtils.getClientFromToken((String) tokenRequest.getToken());
        kafkaProducer.sendUserDetails(new UserDetailsDto(client.getEmail(), client.getPassword(), true, true, true, true, List.of(client.getRole().name())));
    }
}

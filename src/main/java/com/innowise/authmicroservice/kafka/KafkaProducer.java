package com.innowise.authmicroservice.kafka;

import com.innowise.authmicroservice.avro.TokenResponse;
import com.innowise.authmicroservice.avro.UserDetailsDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducer {

    @Value(value = "${kafka.topics.user_details}")
    private String topicUserDetails;
    @Value(value = "${kafka.topics.token_response}")
    private String topicTokenResponse;

    @NonNull
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendTokenResponse(TokenResponse tokenResponse) {
        kafkaTemplate.send(topicTokenResponse, tokenResponse);
        System.out.println("Token response was sent!");
    }

    public void sendUserDetails(UserDetailsDto userDetailsDto) {
        kafkaTemplate.send(topicUserDetails, userDetailsDto);
        System.out.println("User details was sent!");
    }
}

package com.innowise.authmicroservice.kafka;

import avro.UserDetailsResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducer {

    @Value(value = "${kafka.topics.user_details_response}")
    private String topicUserDetailsResponse;

    @NonNull
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendUserDetailsResponse(UserDetailsResponse userDetailsResponse) {
        kafkaTemplate.send(topicUserDetailsResponse, userDetailsResponse);
        System.out.println("Token response was sent!");
    }
}

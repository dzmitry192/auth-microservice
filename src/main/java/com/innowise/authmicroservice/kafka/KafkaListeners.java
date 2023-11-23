package com.innowise.authmicroservice.kafka;

import avro.UserDetails;
import avro.UserDetailsRequest;
import avro.UserDetailsResponse;
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

    @KafkaListener(topics = "${kafka.topics.user_details_request}", groupId = "user_details_request_id", containerFactory = "listenerContainerFactory")
    public void userDetailsRequestListener(UserDetailsRequest userDetailsRequest) {
        if (jwtUtils.validateAccessToken(userDetailsRequest.getToken().toString())) {
            ClientEntity client = jwtUtils.getClientFromToken(userDetailsRequest.getToken().toString());
            kafkaProducer.sendUserDetailsResponse(new UserDetailsResponse(200, new UserDetails(Math.toIntExact(client.getId()), client.getEmail(), client.getPassword(), true, true, true, true, List.of(client.getRole().name()))));
        } else {
            kafkaProducer.sendUserDetailsResponse(new UserDetailsResponse(401, null));
        }
    }
}

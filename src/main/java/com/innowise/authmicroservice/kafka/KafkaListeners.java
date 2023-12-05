package com.innowise.authmicroservice.kafka;

import avro.DeleteClientResponse;
import avro.UserDetails;
import avro.UserDetailsRequest;
import avro.UserDetailsResponse;
import com.innowise.authmicroservice.entity.ClientEntity;
import com.innowise.authmicroservice.utils.JwtUtils;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CountDownLatch;

@Component
@RequiredArgsConstructor
public class KafkaListeners {

    private final JwtUtils jwtUtils;
    private final KafkaProducer kafkaProducer;

    private final CountDownLatch countDownLatch = new CountDownLatch(1);
    private DeleteClientResponse deleteClientResponse;

    @KafkaListener(topics = "${kafka.topics.user_details_request}", groupId = "user_details_request_id", containerFactory = "listenerContainerFactory")
    public void userDetailsRequestListener(UserDetailsRequest userDetailsRequest) {
        if (jwtUtils.validateAccessToken(userDetailsRequest.getToken().toString())) {
            ClientEntity client = jwtUtils.getClientFromToken(userDetailsRequest.getToken().toString());
            kafkaProducer.sendUserDetailsResponse(new UserDetailsResponse(200, new UserDetails(Math.toIntExact(client.getId()), client.getEmail(), client.getPassword(), true, true, true, true, List.of(client.getRole().name()))));
        } else {
            kafkaProducer.sendUserDetailsResponse(new UserDetailsResponse(401, null));
        }
    }

    @KafkaListener(topics = "${kafka.topics.delete_client_response}", groupId = "delete_client_response_id", containerFactory = "listenerContainerFactory")
    public void deleteClientResponseListener(DeleteClientResponse deleteClientResponse) {
        this.deleteClientResponse = deleteClientResponse;
        countDownLatch.countDown();
    }

    public DeleteClientResponse waitForDeleteClientResponse() throws InterruptedException {
        countDownLatch.await();
        return deleteClientResponse;
    }
}

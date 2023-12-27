package com.innowise.authmicroservice.kafka;

import avro.ClientActionRequest;
import avro.DeleteClientRequest;
import avro.NotificationRequest;
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
    @Value(value = "${kafka.topics.delete_client_request}")
    private String topicDeleteClientRequest;
    @Value(value = "${kafka.topics.notification_request}")
    private String topicNotificationRequest;
    @Value(value = "${kafka.topics.user_action_request}")
    private String topicClientActionRequest;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendUserDetailsResponse(UserDetailsResponse userDetailsResponse) {
        kafkaTemplate.send(topicUserDetailsResponse, userDetailsResponse);
    }

    public void sendDeleteClientRequest(DeleteClientRequest deleteClientRequest) {
        kafkaTemplate.send(topicDeleteClientRequest, deleteClientRequest);
    }

    public void sendNotificationRequest(NotificationRequest notificationRequest) {
        kafkaTemplate.send(topicNotificationRequest, notificationRequest);
    }

    public void sendClientActionRequest(ClientActionRequest clientActionRequest) {
        kafkaTemplate.send(topicClientActionRequest, clientActionRequest);
    }
}

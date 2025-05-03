package org.maia.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.maia.domain.User;
import org.maia.enums.EventType;
import org.maia.model.Envelope;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

@Slf4j
@Service
public class UserPublisher {

    private final SnsClient snsClient;
    private final String userEventTopicArn;
    private final ObjectMapper objectMapper;

    public UserPublisher(SnsClient snsClient,
                         @Qualifier("userEventsTopicArn") String userEventTopicArn,
                         ObjectMapper objectMapper) {
        this.snsClient = snsClient;
        this.userEventTopicArn = userEventTopicArn;
        this.objectMapper = objectMapper;
    }

    public void publishUserEvent(User user, EventType eventType, String userName) {
        try {
            String messageBody = buildEnvelopeJson(user, eventType);
            PublishRequest request = PublishRequest.builder()
                    .topicArn(userEventTopicArn)
                    .message(messageBody)
                    .subject("User Event: " + eventType.name())
                    .build();

            snsClient.publish(request);
            log.info("User event published to SNS: {}", user.getEmail());

        } catch (JsonProcessingException e) {
            log.error("Failed to serialize event for user {}", user.getEmail(), e);
        } catch (Exception e) {
            log.error("Unexpected error when publishing user event", e);
        }
    }

    private String buildEnvelopeJson(User user, EventType eventType) throws JsonProcessingException {
        Envelope envelope = Envelope.builder()
                .eventType(eventType)
                .data(objectMapper.writeValueAsString(user))
                .build();
        return objectMapper.writeValueAsString(envelope);
    }
}

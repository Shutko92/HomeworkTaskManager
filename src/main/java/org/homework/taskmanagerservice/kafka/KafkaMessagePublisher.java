package org.homework.taskmanagerservice.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.homework.taskmanagerservice.dto.MessageEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaMessagePublisher {
    private final KafkaTemplate<String, MessageEvent> kafkaTemplate;
    @Value("${spring.topic.my_application.name}")
    private String topic;

    public void sendMessage(MessageEvent message) {
        CompletableFuture<SendResult<String, MessageEvent>> future = kafkaTemplate.send(topic, message);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Sent message=[{}] to topic=[{}]", message, result.getProducerRecord().topic());
            } else {
                log.error("Unable to send message=[{}] due to: {}", message, ex.getMessage());
            }
        });
    }
}

package org.homework.taskmanagerservice.kafka;

import lombok.RequiredArgsConstructor;
import org.homework.taskmanagerservice.dto.MessageEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class KafkaMessagePublisher {
    private final KafkaTemplate<String, MessageEvent> kafkaTemplate;
    @Value("${spring.topic.my_application.name}")
    private String topic;

    public void sendMessage(MessageEvent message) {
        CompletableFuture<SendResult<String, MessageEvent>> future = kafkaTemplate.send(topic, message);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("Sent message=[" + message + "] to topic=["+ result.getProducerRecord().topic() + "]");
            } else {
                System.out.println("Unable to send message=[" + message + "] due to: " + ex.getMessage());
            }
        });
    }
}

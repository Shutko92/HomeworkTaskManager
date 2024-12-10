package org.homework.taskmanagerservice.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.homework.taskmanagerservice.dto.MessageEvent;
import org.homework.taskmanagerservice.service.MailNotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaMessageListener {
    private final MailNotificationService mailNotificationService;

    @KafkaListener(topics = "${spring.topic.my_application.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(MessageEvent message, Acknowledgment acknowledgment) {
        try {
            acknowledgment.acknowledge();
            log.info("Consumed and acknowledged message: {}", message.toString());

            String mailTo = "ilya.shutko@gmail.com";
            String subject = "Status change of a task in TaskManagerService";
            String body = "The status of the task with Id %d was changed to: %s";
            mailNotificationService.sendMail(mailTo, subject, body.formatted(message.getTaskId(), message.getStatus()));
        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
        }
    }
}

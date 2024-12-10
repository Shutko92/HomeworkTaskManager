package org.homework.taskmanagerservice.service;

public interface MailNotificationService {
    void sendMail(String to, String subject, String body);
}

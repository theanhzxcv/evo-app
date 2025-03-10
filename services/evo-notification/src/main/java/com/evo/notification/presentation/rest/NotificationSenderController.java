package com.evo.notification.presentation.rest;

import com.evo.dto.event.EmailNotificationEvent;
import com.evo.dto.event.PushNotificationEvent;
import com.evo.notification.application.service.EmailNotificationService;
import com.evo.notification.application.service.PushNotificationService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationSenderController {

    private static final String TOPIC = "notification-delivery";
    private final EmailNotificationService emailNotificationService;
    private final PushNotificationService pushNotificationService;

    @KafkaListener(topics = TOPIC)
    @RetryableTopic(
            attempts = "5",
            backoff = @Backoff(delay = 1000, multiplier = 2),
            dltTopicSuffix = "_DLT"
    )
    public void signUpSuccessful(EmailNotificationEvent event) throws MessagingException {
        emailNotificationService.sendSuccessSignUpEmail(event);
    }

    @KafkaListener(topics = TOPIC)
    public void forgotPassword(EmailNotificationEvent event) throws MessagingException {

        emailNotificationService.sendResetPasswordEmail(event);
    }

    @KafkaListener(topics = TOPIC)
    public void changePassword(EmailNotificationEvent event) throws MessagingException {

        emailNotificationService.sendChangePasswordEmail(event);
    }

    public String sendPushNotification(@ParameterObject PushNotificationEvent event) {
        return pushNotificationService.sendNotification(event);
    }
}

package com.evo.notification.application.service;

import com.evo.dto.event.EmailNotificationEvent;
import jakarta.mail.MessagingException;

public interface EmailNotificationService {

    void sendSuccessSignUpEmail(EmailNotificationEvent event) throws MessagingException;

    void sendResetPasswordEmail(EmailNotificationEvent event) throws MessagingException;

    void sendChangePasswordEmail(EmailNotificationEvent event) throws MessagingException;
}

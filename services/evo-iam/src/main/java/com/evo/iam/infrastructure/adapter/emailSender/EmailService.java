package com.evo.iam.infrastructure.adapter.emailSender;

import jakarta.mail.MessagingException;

public interface EmailService {

    void sendResetPasswordEmail(String email, String token) throws MessagingException;
}

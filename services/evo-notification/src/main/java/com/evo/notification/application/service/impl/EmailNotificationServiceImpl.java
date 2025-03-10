package com.evo.notification.application.service.impl;

import com.evo.dto.event.EmailNotificationEvent;
import com.evo.notification.application.service.EmailNotificationService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailNotificationServiceImpl implements EmailNotificationService {

    private final JavaMailSender mailSender;

    @Override
    public void sendSuccessSignUpEmail(EmailNotificationEvent event) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true);

        mimeMessageHelper.setTo(event.getRecipient());
        mimeMessageHelper.setSubject(event.getSubject());

        String emailContent = event.getBody();

        mimeMessageHelper.setText(emailContent, true);
        mailSender.send(message);
    }

    @Override
    public void sendResetPasswordEmail(EmailNotificationEvent event) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true);

        mimeMessageHelper.setTo(event.getRecipient());
        mimeMessageHelper.setSubject(event.getSubject());

        String emailContent = event.getBody();

        mimeMessageHelper.setText(emailContent, true);
        mailSender.send(message);
    }

    @Override
    public void sendChangePasswordEmail(EmailNotificationEvent event) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true);

        mimeMessageHelper.setTo(event.getRecipient());
        mimeMessageHelper.setSubject(event.getSubject());

        String emailContent = event.getBody();

        mimeMessageHelper.setText(emailContent, true);
        mailSender.send(message);
    }
}

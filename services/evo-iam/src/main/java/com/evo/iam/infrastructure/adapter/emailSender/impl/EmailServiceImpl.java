package com.evo.iam.infrastructure.adapter.emailSender.impl;

import com.evo.iam.infrastructure.adapter.emailSender.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendResetPasswordEmail(String email, String token) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true);

        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Reset Your Password");

        String resetLink = "http://localhost:8081/iam/users/reset-password?token=" + token;
        String emailContent = """
        <div style="font-family: Arial, sans-serif; font-size: 14px; color: #333;">
            <p>Hello,</p>
            <p>You requested to reset your password. Click the link below to proceed:</p>
            <p>
                <a href="%s" target="_blank" 
                   style="display: inline-block; padding: 10px 15px; color: white; background-color: #007bff; 
                          text-decoration: none; border-radius: 5px;">
                    Reset Password
                </a>
            </p>
            <p>If you did not request this, please ignore this email.</p>
            <p>Thanks,<br/>IAM Service</p>
        </div>
        """.formatted(resetLink);

        mimeMessageHelper.setText(emailContent, true);
        mailSender.send(message);
    }

}

package com.evo.notification.application.service.impl;

import com.evo.dto.event.PushNotificationEvent;
import com.evo.notification.application.service.PushNotificationService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PushNotificationServiceImpl implements PushNotificationService {

    private final FirebaseMessaging firebaseMessaging;

    @Override
    public String sendNotification(PushNotificationEvent event) {
        Notification notification = Notification.builder()
                .setTitle(event.getTitle())
                .setBody(event.getBody())
                .setImage(event.getImage())
                .build();

        Message message = Message.builder()
                .setToken(event.getRecipientToken())
                .setNotification(notification)
                .putAllData(event.getData())
                .build();
        try {
            firebaseMessaging.send(message);
            return "Success Sending Notification";
        } catch (Exception e) {
            return "Error Sending Notification" + e.getMessage();
        }
    }
}

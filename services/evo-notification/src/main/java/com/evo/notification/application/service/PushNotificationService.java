package com.evo.notification.application.service;

import com.evo.dto.event.PushNotificationEvent;

public interface PushNotificationService {

    String sendNotification(PushNotificationEvent event);
}

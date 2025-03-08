package com.amit.converse.chat.service.Notification;

import com.amit.converse.chat.dto.Notification.UserInactiveNotification;
import com.amit.converse.chat.model.Enums.ConnectionStatus;
import org.springframework.stereotype.Service;

@Service
public class UserInactiveNotificationService extends UserNotificationService {

    public UserInactiveNotificationService(WebSocketMessageService webSocketMessageService) {
        super(webSocketMessageService);
    }

    public void sendNotification(String userId) {
        super.sendNotification(userId, UserInactiveNotification.builder().status(ConnectionStatus.OFFLINE).build());
    }
}

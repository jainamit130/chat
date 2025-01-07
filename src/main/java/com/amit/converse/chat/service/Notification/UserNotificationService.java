package com.amit.converse.chat.service.Notification;

import com.amit.converse.chat.dto.Notification.IUserNotification;
import org.springframework.stereotype.Service;

@Service
public class UserNotificationService extends NotificationService {

    public UserNotificationService(WebSocketMessageService webSocketMessageService) {
        super(webSocketMessageService);
    }

    @Override
    protected final String getBaseAddress() {
        return "/user";
    }

    public void sendNotification(String userId,IUserNotification notification) {
        webSocketMessageService.sendNotification(getAddress(userId),notification);
    }
}
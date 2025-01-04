package com.amit.converse.chat.service.Notification;

import org.springframework.stereotype.Service;

@Service
public class UserNotificationService extends NotificationService {
    public UserNotificationService(WebSocketMessageService webSocketMessageService) {
        super(webSocketMessageService);
    }

    @Override
    protected String getBaseAddress() {
        return "/user";
    }
}

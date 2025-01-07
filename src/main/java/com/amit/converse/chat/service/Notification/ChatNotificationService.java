package com.amit.converse.chat.service.Notification;

import com.amit.converse.chat.dto.Notification.IChatNotification;
import org.springframework.stereotype.Service;

@Service
public class ChatNotificationService extends NotificationService {

    public ChatNotificationService(WebSocketMessageService webSocketMessageService) {
        super(webSocketMessageService);
    }

    @Override
    protected final String getBaseAddress() {
        return "/chat";
    }

    public void sendNotification(String chatRoomId,IChatNotification notification) {
        webSocketMessageService.sendNotification(getAddress(chatRoomId),notification);
    }

    // ChatTransaction Notification
    // Message Delete Notification
    // Message Notification
    // Message Marked Notification

}

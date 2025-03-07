package com.amit.converse.chat.service.Notification;

import com.amit.converse.chat.dto.Notification.InactiveChatRoomNotification;
import com.amit.converse.chat.model.Enums.ConnectionStatus;
import org.springframework.stereotype.Service;

@Service
public class InactiveChatRoomNotificationService extends UserNotificationService {

    public InactiveChatRoomNotificationService(WebSocketMessageService webSocketMessageService) {
        super(webSocketMessageService);
    }

    public void sendNotification(String userId) {
        super.sendNotification(userId, InactiveChatRoomNotification.builder().status(ConnectionStatus.INACTIVE).build());
    }
}

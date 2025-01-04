package com.amit.converse.chat.service.Notification;

import com.amit.converse.chat.dto.Notification.ChatTransactionNotification;
import com.amit.converse.chat.dto.Notification.INotification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatNotificationService extends NotificationService {
    public ChatNotificationService(WebSocketMessageService webSocketMessageService) {
        super(webSocketMessageService);
    }

    @Override
    protected String getBaseAddress() {
        return "/chat";
    }

    @Override
    protected INotification getNotification() {
        ChatTransactionNotification notification = ChatTransactionNotification.builder(transactionNotifications).build();
        return notification;
    }


    public void notifyChatTransaction(String chatRoomId, List<String> transactionNotifications) {
        webSocketMessageService.sendNotification(getAddress(chatRoomId),getNotification());
    }

    public void notifyChatMessageDeletion(String chatRoomId, List<String> messageDeletions) {
        webSocketMessageService.sendNotification(getAddress(chatRoomId),getNotification());
    }
}

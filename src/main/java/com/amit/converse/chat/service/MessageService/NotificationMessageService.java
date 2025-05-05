package com.amit.converse.chat.service.MessageService;

import com.amit.converse.chat.model.Messages.NotificationMessage;

import java.time.Instant;

public class NotificationMessageService {

    public static NotificationMessage generateNotificationMessage(String chatRoomId,String content) {
        NotificationMessage notificationMessage = new NotificationMessage();
        notificationMessage.setContent(content);
        notificationMessage.setChatRoomId(chatRoomId);
        notificationMessage.setTimestamp(Instant.now());
        return notificationMessage;
    }
}

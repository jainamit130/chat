package com.amit.converse.chat.dto.Notification;

import java.util.List;

public class MessageReadNotification extends MessageMarkedNotification {
    public MessageReadNotification(String chatRoomId, List<String> messageIds) {
        super(chatRoomId,messageIds);
    }

    @Override
    public NotificationType getNotificationType() {
        return NotificationType.MESSAGE_READ;
    }
}

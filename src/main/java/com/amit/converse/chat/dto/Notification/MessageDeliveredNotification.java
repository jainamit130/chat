package com.amit.converse.chat.dto.Notification;

import java.util.List;

public class MessageDeliveredNotification extends MessageMarkedNotification {
    public MessageDeliveredNotification(String chatRoomId, List<String> messageIds) {
        super(chatRoomId,messageIds);
    }

    @Override
    public NotificationType getNotificationType() {
        return NotificationType.MESSAGE_DELIVERED;
    }
}

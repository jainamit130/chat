package com.amit.converse.chat.dto.Notification;

import lombok.Builder;

import java.util.List;

@Builder
public class ChatTransactionNotification extends IChatNotification {
    private List<String> notifications;

    @Override
    public NotificationType getNotificationType() {
        return NotificationType.TRANSACTION;
    }
}

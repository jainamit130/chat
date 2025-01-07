package com.amit.converse.chat.dto.Notification;

import lombok.Builder;

import java.util.List;

@Builder
public class ChatTransactionNotification implements IChatNotification {
    private List<String> notifications;
}

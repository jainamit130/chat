package com.amit.converse.chat.dto.Notification;

import lombok.Builder;

import java.util.List;

@Builder
public class MessageMarkedNotification implements IChatNotification {
    private String senderId;
    private List<String> messageIds;
}

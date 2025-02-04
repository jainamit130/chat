package com.amit.converse.chat.dto.Notification;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class MessageMarkedNotification implements IChatNotification {
    private String senderId;
    private List<String> messageIds;
}

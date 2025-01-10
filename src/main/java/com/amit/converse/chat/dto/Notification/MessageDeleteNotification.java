package com.amit.converse.chat.dto.Notification;

import lombok.Builder;

import java.util.List;

@Builder
public class MessageDeleteNotification implements IChatNotification {
    private List<String> messageId;
}

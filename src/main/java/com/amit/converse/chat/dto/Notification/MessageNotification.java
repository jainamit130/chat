package com.amit.converse.chat.dto.Notification;

import com.amit.converse.chat.model.Messages.ChatMessage;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MessageNotification implements IChatNotification {
    private ChatMessage message;
}

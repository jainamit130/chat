package com.amit.converse.chat.dto.Notification;

import com.amit.converse.chat.model.Messages.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class MessageNotification extends IChatNotification {
    private ChatMessage message;

    @Override
    public NotificationType getNotificationType() {
        return NotificationType.MESSAGE;
    }
}

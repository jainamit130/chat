package com.amit.converse.chat.dto.Notification;

import lombok.Builder;

import java.util.List;

@Builder
public class TypingNotification extends IChatNotification {
    List<String> typingUsernames;

    @Override
    public NotificationType getNotificationType() {
        return NotificationType.TYPING;
    }
}

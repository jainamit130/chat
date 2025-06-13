package com.amit.converse.chat.dto.Notification;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class TypingNotification extends IChatNotification {
    List<String> typingUsernames;
    String chatRoomId;

    @Override
    public NotificationType getNotificationType() {
        return NotificationType.TYPING;
    }
}

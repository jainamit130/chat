package com.amit.converse.chat.dto.Notification;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class MessageDeleteNotification extends IChatNotification {
    private List<String> messageIds;

    @Override
    public NotificationType getNotificationType() {
        return NotificationType.MESSAGE_DELETED;
    }
}

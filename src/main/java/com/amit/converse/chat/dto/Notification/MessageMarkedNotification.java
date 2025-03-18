package com.amit.converse.chat.dto.Notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MessageMarkedNotification extends IUserNotification {
    private String chatRoomId;
    private List<String> messageIds;

    @Override
    public NotificationType getNotificationType() {
        return NotificationType.MESSAGE_MARKED;
    }
}

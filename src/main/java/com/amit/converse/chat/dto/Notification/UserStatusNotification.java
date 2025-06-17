package com.amit.converse.chat.dto.Notification;

import com.amit.converse.chat.model.Enums.ConnectionStatus;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserStatusNotification extends IChatNotification {
    private String username;
    private ConnectionStatus status;
    private String chatRoomId;

    @Override
    public NotificationType getNotificationType() {
        return NotificationType.STATUS;
    }
}

package com.amit.converse.chat.dto.Notification;

import com.amit.converse.chat.model.Enums.ConnectionStatus;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserOnlineStatusNotification implements IChatNotification {
    private String username;
    private ConnectionStatus status;

    public UserOnlineStatusNotification(String username, ConnectionStatus status) {
        this.username = username;
        this.status = status;
    }
}

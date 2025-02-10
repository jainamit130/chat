package com.amit.converse.chat.dto.Notification;

import com.amit.converse.chat.model.Enums.ConnectionStatus;
import lombok.Builder;

@Builder
public class UserOnlineNotification implements IChatNotification {
    private String username;
    private ConnectionStatus status;

    public UserOnlineNotification(String username, ConnectionStatus status) {
        this.username = username;
        this.status = status;
    }
}

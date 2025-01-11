package com.amit.converse.chat.dto.Notification;

import com.amit.converse.chat.model.Enums.ConnectionStatus;
import lombok.Builder;
import org.springframework.stereotype.Component;

@Component
@Builder
public class UserOnlineNotification implements IChatNotification {
    private String username;
    private ConnectionStatus status;
}

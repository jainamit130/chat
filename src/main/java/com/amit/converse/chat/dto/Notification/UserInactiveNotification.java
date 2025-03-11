package com.amit.converse.chat.dto.Notification;

import com.amit.converse.chat.model.Enums.ConnectionStatus;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserInactiveNotification implements IUserNotification {
    ConnectionStatus status;
}

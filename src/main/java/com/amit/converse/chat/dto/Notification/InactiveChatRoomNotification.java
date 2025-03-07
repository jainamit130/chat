package com.amit.converse.chat.dto.Notification;

import com.amit.converse.chat.model.Enums.ConnectionStatus;
import lombok.Builder;

@Builder
public class InactiveChatRoomNotification implements IUserNotification {
    ConnectionStatus status;
}

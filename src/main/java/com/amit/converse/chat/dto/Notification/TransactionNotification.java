package com.amit.converse.chat.dto.Notification;

import com.amit.converse.chat.dto.OnlineUsers.IOnlineUsersDTO;
import com.amit.converse.chat.model.Enums.ConnectionStatus;
import com.amit.converse.chat.model.Messages.NotificationMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionNotification {
    NotificationMessage message;
    String username;
    String moderatorName;
    String id;
    ConnectionStatus onlineStatus;
    IOnlineUsersDTO onlineUsersDTO;
    NotificationType type;
}
